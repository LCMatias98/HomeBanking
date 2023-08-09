const { createApp } = Vue;

createApp({
  data() {
    return {
      clients: [],
      loans: [],
      accounts: [],
      accountsNotHidden:[],
      accountsFilter:[],
      loanDTO: {
        name: '',
        amount: 0.0,
        payment: [],
        interest: 0.0
      },
      interest:[],
      err: '',
      showConfirmation: false
    };
  },
/* const listaInput = document.getElementById("listaInput").value;
            const numeros = listaInput.split(",").map(Number);


    private String name;
    private Double amount;
    private List<Integer> payment;

    private Double interest;
             */
  created() {
    axios.get('/api/clients/current')
      .then(res => {
        
        console.log(res);
        this.clients = res.data;
        this.loans = this.clients.loans;
        this.accounts = this.clients.accounts.sort((a , b) => a.id - b.id );
        this.accountsNotHidden = this.clients.accounts.filter(account => account.hidden === false).sort((a , b) => a.id - b.id );
        console.log(this.accounts);
      })
      .catch(error => {
        console.error(error);
      });

/*       this.interest = this.loanDTO.payment.split(',').map((payment) => parseInt(payment.trim(), 10));
      console.log(interest) */
   
  },
/*   computed(){
    this.accountsFilter = accounts.filter(acc => acc.number !== this.transferDTO.accountOrigin);
  }, */

  methods: {
 
        createLoan(event) {
          event.preventDefault();
          this.showConfirmation = true;
        },

        

    confirmCreateCard() { 
        this.showConfirmation = false; 

        this.interest = this.loanDTO.payment.split(',').map((payment) => parseInt(payment.trim(), 10));
        console.log(interest)
/*         payment: this.loanDTO.payment.split(',').map((payment) => parseInt(payment.trim(), 10)), */
        const transferData = {
          name: this.loanDTO.name,
          amount: this.loanDTO.amount,
          payment: this.interest,
          interest: this.loanDTO.interest
        };
        console.log(this.transferData)
          axios.post('/api/loans/create', transferData)
            .then(res => {
              console.log(res);
              this.status = res.status;
              if (this.status === 201) {
                this.showNotification('Transaction success', 'success');
                setTimeout(() => {window.location.href = './pay-loan.html';
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
