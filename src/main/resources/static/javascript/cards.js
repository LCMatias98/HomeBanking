const { createApp } = Vue
 
createApp({
    data() {
      return {
        client:[],
        cards:[]
        }
      },
    created(){
        this.loadData();
    },

    methods:{
        loadData(){
            axios.get('http://localhost:8080/api/clients/1')
                .then(res=> {
                this.client = res.data;
                this.cards = this.client.cards;
                console.log(this.client);
                console.log(this.cards);
                })
                .catch(error => {
                    console.error(error);
                  });
        },


    },



})
.mount('#app');