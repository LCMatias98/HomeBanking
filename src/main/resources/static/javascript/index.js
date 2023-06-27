const { createApp } = Vue;

createApp({
  data() {
    return {
      email: '',
      password: ''
    }
  },

  created() {
/*     this.submitForm(); */
  },
/* {headers:{'content-type':'application/x-www-form-urlencoded'}} */
  methods: {
    submitForm(event) {
      event.preventDefault();
      axios.post('/api/login', `email=${this.email}&password=${this.password}`)
        .then(response => {
/*           console.log(response.data); */
          window.location.href = './accounts.html';
        })
        .catch(error => {
          // Manejo de errores
          console.error(error);
        });
    }
  },
}).mount('#app');
