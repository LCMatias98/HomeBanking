const { createApp } = Vue;

createApp({
  data() {
    return {
        clients: [],
        accountType: "",
        showConfirmation: false,
        err: ''
    }
  },
  methods: {
    createAccount() {
      this.showConfirmation = true
    },
    confirmCreateAccount() {
      this.showConfirmation = false;
        /* console.log(this.accountType) */
      axios.post('/api/clients/current/accounts', `accountType=${this.accountType}`)
        .then((res) => {
          if (res.status === 201) {
            this.showNotification('Account Created', 'success');
            setTimeout(() => {
              window.location.href = './accounts.html';
            }, 700);
          }
        })
        .catch(error => {
            console.error(error);
            this.err = error.response.data;
            console.log(this.err)
            this.showNotification(this.err, 'error');
          });
    
    },
    cancelCreateAccount() {
      this.showConfirmation = false;
    },
    logOut() {
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
