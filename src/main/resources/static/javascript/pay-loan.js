const { createApp } = Vue;

createApp({
  data() {
    return {
      clients: [],
      loans: [],
      filteredLoans:[],
      accounts: [],
      accountsNotHidden:[],
      accountsFilter:[],
      interest:[],
      err: '',
      showConfirmation: false,
      payLoan:'',
      params:'',
      accountToPay:'',
/*       val:0,
      value:'' */
    };
  },

    created() {
        axios.get('/api/clients/current')
          .then(res => {
            this.clients = res.data;
            this.loans = this.clients.loans;
            this.accounts = this.clients.accounts.sort((a , b) => a.id - b.id );
            this.accountsNotHidden = this.clients.accounts.filter(account => account.hidden === false).sort((a , b) => a.id - b.id );
            console.log("Loans:", this.loans);
            this.params = new URLSearchParams(location.search).get("id");
            console.log("ID from URL:", this.params);

/*             this.val = filteredLoans[0].amount / filteredLoans[0].payments
            this.value = "$" + this.val.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g, ',')
            console.log(this.val) */
            // Verify that the loans and params are set correctly before filtering
            if (this.loans && this.params) {
              this.filteredLoans = this.loans.filter((loan) => loan.id == this.params);
            } else {
              console.log("Loans or ID is missing");
            }
      
            // Rest of the code...
      
            console.log("Filtered Loans:", this.filteredLoans);
            console.log(this.accountToPay);
            console.log(this.params)
          })
          .catch(error => {
            console.error(error);
          });
      },

  methods: {
 
    payLoanAmount(event) {
        event.preventDefault();
        this.showConfirmation = true;
    },      

    confirmPayLoan() { 
        console.log(this.accountToPay);
        console.log(this.params)
        this.showConfirmation = false; 
          axios.post('/api/client-loans/payment',`numberAccount=${this.accountToPay}&id=${this.params}`)
            .then(res => {
              console.log(res);
              this.status = res.status;
              if (this.status === 200) {
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
     
    cancelPayLoan() {
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

/*       this.interest = this.loanDTO.payment.split(',').map((payment) => parseInt(payment.trim(), 10));
      console.log(interest) */
/*   computed(){
    this.accountsFilter = accounts.filter(acc => acc.number !== this.transferDTO.accountOrigin);
  }, */

  /* const listaInput = document.getElementById("listaInput").value;
            const numeros = listaInput.split(",").map(Number);


    private String name;
    private Double amount;
    private List<Integer> payment;

    private Double interest;
             */