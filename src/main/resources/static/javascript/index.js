// https://mindhub-xj03.onrender.com/api/amazing

const { createApp } = Vue

  createApp({
    // Propiedades reactivas
    data() {
      return {
        clients:[]
      }

    },

    created(){
        /* this.loadData(); */
        axios.get('http://localhost:8080/api/clients/1')
        .then(res=> {
        this.clients = res.data;
        console.log(this.clients);
        })
    },

    methods:{

        hasTwoAccounts(client) {
          return client.accounts.length >= 2;
        }
/*     
          hasTwoAccounts(client) {
            const accountKeys = Object.keys(client.accounts);
            return accountKeys.length >= 2;
          } */
        
    }


  }).mount('#app')