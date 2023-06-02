// https://mindhub-xj03.onrender.com/api/amazing

const { createApp } = Vue

  createApp({
    // Propiedades reactivas
    data() {
      return {
        clients:[],
        clientRestResponse:[],
        clientData:{
        firstName:"",
        lastName:"",
        email:""
        }
      }

    },

    created(){
        this.loadData();
    },

    methods:{
        loadData(){
            axios.get('http://localhost:8080/clients')
                .then(res=> {
                this.clients = res.data._embedded.clients;
                this.clientRestResponse = res.data;
                console.log(this.clients);
                })
        },

        addClient(){
            this.postClient();
        },

        postClient(){
            axios.post('http://localhost:8080/clients', this.clientData)
            .then(res=> {
                this.loadData();
                this.deleteInputs();
             }
  )},

        deleteInputs(){
         this.clientData.firstName = "";
         this.clientData.lastName = "";
         this.clientData.email = "";
        },

        deleteClients(id){
          axios.delete(id)
            .then(response =>{
              this.loadData();
          })
          .catch(err=>{console.error(err)});

        },

         updateClients(id){
                  axios.patch(id, this.clientData)
                    .then(response =>{
                     this.clientData.firstName = "";
                     this.clientData.lastName = "";
                     this.clientData.email = "";
                      this.loadData();
                  })
                  .catch(err=>{console.error(err)});

         }
    },


  }).mount('#app')