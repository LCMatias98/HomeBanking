const { createApp } = Vue;

createApp({
  data() {
    return {
      email: '',
      password: '',
      firstName: '',
      lastName: '',
      status: '',
      err: ''
    }
  },

  created() {
 /*    this.submitForm({ preventDefault: () => {} }); */

/*  this.submitForm(); */
  },

  methods: {
    submitForm(event) {
      event.preventDefault();
      
      axios.post('/api/clients', `firstName=${this.firstName}&lastName=${this.lastName}&email=${this.email}&password=${this.password}`)
        .then(response => {
          this.status = response.status;

          
          if (this.status === 201) {
            this.showNotification('Registered Client', 'success');
          }
          
          this.email = '';
          this.password = '';
          this.firstName = '';
          this.lastName = '';
        })
        .catch(error => {
          console.error(error);
          this.err = error.response.data;
          console.log(this.err)
          this.showNotification(this.err, 'error');
        });
    }
    ,
    
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
