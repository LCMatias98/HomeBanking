const { createApp } = Vue;

createApp({
  data() {
    return {
      cardType:[],
      cardColor:[]
    }
  },

  methods:{
    
    createCard(){
      axios.post('/api/clients/current/cards', `cardType=${this.cardType}&cardColor=${this.cardColor}`)
      .then(res => {
        this.status = res.status;
        console.log(this.status)
        if (this.status === 201) {        
        this.showNotification('Card Created', 'success');
        setTimeout(() => {
          window.location.href = './cards.html'; // Redireccionar después de un retraso
        }, 1000);
        }


      })
      .catch(error => {
        console.error(error);
      })

    },

   
    logOut(){
        axios.post('/api/logout')
        .then(res => {
          window.location.href = './index.html';
        })
        .catch(error => {
          console.error(error);
        });
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
      },

  },
}).mount('#app');
