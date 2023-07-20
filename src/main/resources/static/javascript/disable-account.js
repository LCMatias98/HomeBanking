const { createApp } = Vue;

createApp({
  data() {
    return {
      clients: [],
      params: "",
      accounts:[],
      showConfirmation: false,
      cards:[],
      err: ''
    }
  },
  mounted() {
   
    axios.get('/api/clients/current')
      .then(res => {

        this.clients = res.data;
        console.log(this.clients);
        this.loans = this.clients.loans;
        this.accounts = this.clients.accounts.sort((a , b) => a.id - b.id )

        this.cards = this.clients.cards.sort((a , b) => a.id - b.id )

        console.log(this.cards)
      })
      .catch(error => {
        console.error(error);
        this.err = error.response.data;
        console.log(this.err);
        this.showNotification(this.err, 'error');
      });
    

  },

  created(){
    this.params = new URLSearchParams(location.search).get("id");
/*     console.log(this.params); */
    
  },


  methods:{ 


    logOut() {
      axios.post('/api/logout')
        .then(response => {
          window.location.href = './index.html';
        })
        .catch(error => {
          console.error(error);
        });
    },

    disableAccount() {
      this.showConfirmation = true
    },



    confirmDisableAccount() { 
        this.showConfirmation = false;
        console.log(this.params);
        axios.patch('/api/clients/current/accounts', `id=${this.params}`)
        .then((res) => {
          
          if (res.status === 200) {
            this.showNotification('Card Disabled', 'success');
            setTimeout(() => {
              window.location.href = './accounts.html';
            }, 700);
          }
        })
        .catch(error => {
          console.error(error);
          this.err = error.response.data;
          console.log(this.err);
          this.showNotification(this.err, 'error');
        });

    },
  
    cancelDisableAccount() {
      this.showConfirmation = false;
    },
    


    showNotification(message, type) {
      const toast = document.createElement('div');
      toast.classList.add('toastify', type); // Agregar la clase "type"
      toast.textContent = message;
      document.body.appendChild(toast);
    
      setTimeout(() => {
        toast.classList.add('show');
        setTimeout(() => {
          toast.classList.remove('show');
          setTimeout(() => {
            document.body.removeChild(toast);
          }, 300);
        }, 2000);
      }, 100);
    }
    
    
  }
}).mount('#app');
