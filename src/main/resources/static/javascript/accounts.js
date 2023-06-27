const { createApp } = Vue;

createApp({
  data() {
    return {
      clients: [],
      dolarOficial: [],
      loans:[]
    }
  },
  mounted() {
    const cantidadPesos = document.getElementById("cantidad-pesos");
    const tipoCambio = document.getElementById("tipo-cambio");
    const cantidadDolares = document.getElementById("cantidad-dolares");
    const formulario = document.getElementById("formulario");

    formulario.addEventListener("submit", (e) => {
      e.preventDefault();
      const cantidad = parseFloat(cantidadPesos.value);
      const cambio = parseFloat(tipoCambio.value);
      const resultado = cantidad * cambio;
      cantidadDolares.value = resultado.toFixed(2);
    });

    axios.get('/api/clients/current')
      .then(res => {
        console.log(res);
        this.clients = res.data;
        this.loans = this.clients.loans;
        
      })
      .catch(error => {
        console.error(error);
      });

    axios.get("https://www.dolarsi.com/api/api.php?type=valoresprincipales")
      .then(res => {
        this.dolarOficial = res.data[0].casa.compra;
        console.log(this.dolarOficial);
      })
      .catch(error => {
        console.error(error);
      });


  },

  methods:{
    logOut(){
      axios.post('/api/logout')
      .then(res => {
        window.location.href = './index.html';
      })
      .catch(error => {
        console.error(error);
      });
    }
  }
}).mount('#app');
