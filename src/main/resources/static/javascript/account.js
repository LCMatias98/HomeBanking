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
          })
          .catch(error => {
            console.error(error);
          });
        }

  },
}).mount('#app');
