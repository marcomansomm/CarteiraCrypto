const { createApp } = Vue;

const baseUrl = "http://localhost:8080/coins"

const mainContainer = {
    data(){
        return{
            coins:[],
            formCoin: {
                isNew: true,
                name: '',
                price: '',
                quantity: '',
                title: "Cadastrar nova Transação",
                button: "Cadastrar"
            },
            transactions: []
        }
    },
    mounted(){
        this.showAllCoins();
    },
    methods:{
        showAllCoins(){
            axios.get(baseUrl)
                .then(response => {
                    response.data.forEach(item => {
                        this.coins.push(item)
                    })
                })
        },
        saveCoin(){
            this.coins = [];
            this.formCoin.name = this.formCoin.name.toLowerCase();
            this.formCoin.price = this.formCoin.price.replace("R$", '')
                .replace(",", ".").trim();

            if(this.formCoin.name === '' || this.formCoin.price === '' || this.formCoin.quantity === ''){
                toastr.error("Todos os campos são obrigatórios");
                return;
            }

            const coin = {
                name: this.formCoin.name,
                price: this.formCoin.price,
                quantity: this.formCoin.quantity

            }

            const self = this;

            axios.post(baseUrl, coin)
                .then(function (response){
                    toastr.success("Nova Transação cadastrada com sucesso!!")
                })
                .catch(function (error){
                    toastr.error("Não foi possível cadastrar uma nova transação", "Fomulario")
                })
                .then(function (){
                    self.showAllCoins();
                    self.cleanForm();
                })
        },
        cleanForm(){
            this.formCoin.isNew = true
            this.formCoin.name = ''
            this.formCoin.price = ''
            this.formCoin.quantity = ''
            this.formCoin.title = "Cadastrar nova Transação"
            this.formCoin.button = "Cadastrar"
        },
        showTransctions(name){
            this.transactions = {
                coinName: name,
                data: []
            };

            axios.get(baseUrl + "/name")
                .then(response =>{
                    console.log(response)
                    response.data.forEach(item => {
                        this.transactions.data.push({
                            id: item.id,
                            name: item.name,
                            price: item.price.toLocaleString("pt-br", { style: "currency", currency: "BRL" }),
                            quantity: item.quantity,
                            dateTime: item.dateTime
                        })
                    })
                })
                .catch(function (error){
                    toastr.error(error)
                })
        }
    }
}

createApp(mainContainer).mount("#app");