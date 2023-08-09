const { createApp } = Vue;

createApp({
  data() {
    return {
      cardType: "",
      cardColor: "",
      showConfirmation: false,
      err: ''
    }
  },
  methods: {
    createCard() {
      this.showConfirmation = true
    },
    confirmCreateCard() {
      this.showConfirmation = false;
  
      axios.post('/api/clients/current/cards', `cardType=${this.cardType}&cardColor=${this.cardColor}`)
        .then((res) => {
          if (res.status === 201) {
            this.showNotification('Card Created', 'success');
            setTimeout(() => {
              window.location.href = './cards.html';
            }, 700);
          }
        })
      .catch(error => {
        console.error(error);
        this.err = error.response.data;
        console.log(this.err)
        if(this.err.status === 400){
          this.showNotification("Please complete the form", 'error');

        }
      });
    },
    cancelCreateCard() {
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
