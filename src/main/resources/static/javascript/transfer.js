const { createApp } = Vue;

createApp({
  data() {
    return {
      clients: [],
      loans: [],
      accounts: [],
      accountsNotHidden:[],
      accountsFilter:[],
      transferDTO: {
        amount: 0.0,
        accountOrigin: '',
        accountDestination: '',
        description: ''
      },
      selectOption:'',
      err: '',
      showConfirmation: false,
      actualBalance:[]
    };
  },

  created() {
    axios.get('/api/clients/current')
      .then(res => {
        console.log(res);
        this.clients = res.data;
        this.loans = this.clients.loans;
        this.accounts = this.clients.accounts.sort((a, b) => a.id - b.id);
        this.accountsNotHidden = this.clients.accounts.filter(account => account.hidden === false).sort((a, b) => a.id - b.id);
        
        this.actualBalance = this.accountsNotHidden.filter(acc => acc.number == this.transferDTO.accountOrigin);
        console.log(this.actualBalance)
      })
      .catch(error => {
        console.error(error);
      });
  },


  methods: {
    makeTransfer(event) {
      event.preventDefault();
      this.showConfirmation = true
    },

      confirmCreateCard() { 
        this.showConfirmation = false; 
        
        const transferData = {
          amount: this.transferDTO.amount,
          accountOrigin: this.transferDTO.accountOrigin,
          accountDestination: this.transferDTO.accountDestination,
          description: this.transferDTO.description
        };
          axios.post('http://localhost:8080/api/transactions', transferData)
            .then(res => {
              console.log(res);
              this.status = res.status;
              if (this.status === 201) {
                this.showNotification('Transaction success', 'success');
                setTimeout(() => {window.location.href = './accounts.html';
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
     
      cancelCreateCard() {
        this.showConfirmation = false;
      },
      

    logOut() {
      axios.post('/api/logout')
        .then(response => {
          window.location.href = './index.html';
        })
        .catch(error => {
          console.error(error);
        });
    },

    showNotification(message, type) {
      const toast = document.createElement('div');
      toast.classList.add('toastify', type);
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
