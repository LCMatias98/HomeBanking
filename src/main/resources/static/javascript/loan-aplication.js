const { createApp } = Vue;


createApp({
  data() {
    return {
      clients: [],
      loans:[],
      claimLoans:{},
      selecterLoan:{},
      destinationAccount:"",
      payment:"",
      amount: 0,
      accounts:[],
      showConfirmation: false,
      err: ''
    }
  },
  created() {
   
    axios.get('/api/clients/current')
      .then(res => {
        console.log(res);
        this.clients = res.data;
        this.loans = this.clients.loans;
        this.accounts = this.clients.accounts.sort((a , b) => a.id - b.id );
        console.log(this.accounts)
      })
      .catch(error => {
        console.error(error);
      });


      axios.get('/api/loans')
      .then(res => {
        this.claimLoans = res.data;
        console.log( this.claimLoans);
        
      })
      .catch(error => {
        console.error(error);
      });

    
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

    claimLoan() {
      this.showConfirmation = true
  },

  confirmCreateCard() { 
    this.showConfirmation = false;

    const transferData = {
      name: this.selecterLoan.name,
      amount: this.amount,
      payment: this.payment,
      destinationAccount: this.destinationAccount
    };


      axios.post('/api/loans',transferData)
        .then(response => {

          this.showNotification('Loan Claimed', 'success');
          setTimeout(() => {
            window.location.href = './accounts.html';
          }, 700);
          console.log(response.status);
        })
        .catch(error => {
          console.error(error);
          this.err = error.response.data;
          console.log(this.err);
          this.showNotification(this.err, 'error');
        });

  },

  cancelCreateCard() {
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
