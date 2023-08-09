const { createApp } = Vue;

createApp({
  data() {
    return {
      client: [],
      cards: [],
      cardStatus: [],
      selectedTypes: [],
      selectedColors: []
    };
  },
  computed: {
    filteredCards() {
      const selectedTypes = this.selectedTypes.length > 0 ? this.selectedTypes : ['DEBIT', 'CREDIT'];
      const selectedColors = this.selectedColors.length > 0 ? this.selectedColors : ['GOLD', 'SILVER', 'TITANIUM'];

      return this.cardStatus.filter(card => selectedTypes.includes(card.type) && selectedColors.includes(card.color));
    },
  },
  created() {
    this.loadData();
  },
  methods: {
    loadData() {
      axios.get('/api/clients/current')
        .then(res => {
          this.client = res.data;
          this.cards = this.client.cards.sort((a, b) => a.id - b.id);
          this.calculateCardStatus();
          /* console.log(this.client); */
          console.log(this.cardStatus);
        })
        .catch(error => {
          console.error(error);
        });
    },
    calculateCardStatus() {
      const currentDate = new Date();
    
      this.cardStatus = this.cards.map(card => {
        const fromDate = new Date(card.fromDate);
        const thruDate = new Date(card.thruDate);
    
        const status = currentDate < fromDate || currentDate > thruDate
          ? 'expirated'
          : 'activated';
    
        return { ...card, status };
      });
    },
    
    logOut() {
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
