const { createApp } = Vue;

createApp({
  data() {
    return {
      email: '',
      password: '',
      err: ''
    }
  },

  //created() {
/*     this.submitForm(); */
  //},
/* {headers:{'content-type':'application/x-www-form-urlencoded'}} */
  methods: {
    submitForm(event) {
      event.preventDefault();
      axios.post('/api/login', `email=${this.email}&password=${this.password}`)
        .then(response => {
/*           console.log(response.data); */

          if(this.email.includes("admin")){
            window.location.href = './manager.html';
          } else {
            window.location.href= './accounts.html';
          }

        })
        .catch(error => {
          console.error(error);
          this.err = error.response.data.error;
         /*  console.log(this.err) */
          this.showNotification(this.err, 'error');
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
  },
}).mount('#app');
