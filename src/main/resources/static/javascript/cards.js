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
            axios.get('http://localhost:8080/api/clients/current')
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
})
.mount('#app');