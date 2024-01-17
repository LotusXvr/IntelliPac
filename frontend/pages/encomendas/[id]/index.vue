<template>
    <div v-if="encomenda">
        <h2>Detalhes de encomenda #{{ encomenda.id }}</h2>
        <p>Cliente: {{ encomenda.consumidorFinal }}</p>
        <p>Data Encomenda: {{ encomenda.dataEncomenda }}</p>
        <p>Operador Logistica: {{ encomenda.operadorLogistica }}</p>
        <p>Estado: {{ encomenda.estado }}</p>

        <p>Produtos:</p>
        <ul>
            <li v-for="produto in encomenda.produtos" :key="produto.id">
                {{ produto.nome }}
                <br />
                Embalagens de Produto:
                <ul>
                    <li v-for="embalagem in produto.embalagensDeProduto" :key="embalagem.id">
                        {{ tipoEmbalagemToString(embalagem.tipoEmbalagem) }} -
                        {{ embalagem.material }}
                        <br />
                        Sensores:
                        <ul>
                            <li v-for="sensor in embalagem.sensores" :key="sensor.id">
                                {{ sensor.tipo }} - {{ sensor.unidade }}
                                <br>
                                Ultima obserção:
                                <ul>
                                    <li
                                        v-for="observacao in sensor.observacoes"
                                        :key="observacao.id"
                                    >
                                        {{ observacao.timestamp }} - {{ observacao.valor }}
                                    </li>
                                </ul>
                            </li>
                        </ul>
                    </li>
                </ul>
            </li>
        </ul>

        <p v-if="encomenda.embalagensTransporte.length > 0">Embalagens Transporte:</p>
        <ul>
            <li
                v-if="encomenda.embalagensTransporte.length > 0"
                v-for="embalagens in encomenda.embalagensTransporte"
                :key="embalagens.id"
            >
                Material:
                {{ embalagens.material }}
                <br>
                Sensores:
                <ul>
                    <li v-for="sensor in embalagens.sensores" :key="sensor.id">
                        {{ sensor.tipo }} - {{ sensor.unidade }}
                        <br>
                        Ultima obserção:
                        <ul>
                            <li v-for="observacao in sensor.observacoes" :key="observacao.id">
                                {{ observacao.timestamp }} - {{ observacao.valor }}
                            </li>
                        </ul>
                    </li>
                </ul>
            </li>
        </ul>
        <h3 v-if="encomenda.embalagensTransporte.length == 0" style="color: darkcyan">
            A encomenda aguarda a colocação de uma embalagem de transporte
        </h3>
    </div>

    <h2>Error messages:</h2>
    {{ messages }}
</template>
<script setup>
const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: encomenda, error: proErr } = await useFetch(`${api}/encomendas/${id}`)
const messages = ref([])
if (proErr.value) messages.value.push(proErr.value)

const tipoEmbalagemToString = (tipo) => {
    switch (tipo) {
        case 1:
            return "Primária"
        case 2:
            return "Secundária"
        case 3:
            return "Terciária"
        default:
            return "Tipo de embalagem desconhecido"
    }
}
</script>
