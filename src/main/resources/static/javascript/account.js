const { createApp } = Vue;

createApp({
  data() {
    return {
      data: [],
      transaction:[],
      params: "",
    }
  },

  created(){
        this.params = new URLSearchParams(location.search).get("id");
        console.log(this.params);
        this.cuentaCliente();

  },

  methods: {

      cuentaCliente(){
        axios.get(`/api/accounts/`+this.params)
          .then(res => {
            this.data = res.data;
            this.transaction = this.data.transaction;
            console.log(this.data)
          })
          .catch(error => {
            console.error(error);
          });
        },
        
      logOut(){
        axios.post('/api/logout')
        .then(res => {
          window.location.href = './index.html';
        })
        .catch(error => {
          console.error(error);
        });
      }

  },
}).mount('#app');
