<template>
    <Navbar />
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
                                <br />
                                <nuxt-link :to="`/sensores/${sensor.id}`"
                                    >Ver mais detalhes</nuxt-link
                                >
                                <br />
                                <div v-if="sensor.observacoes.length > 0">
                                    Ultima observação:
                                    <ul>
                                        <li>
                                            {{ sensor.observacoes[0].timestamp }} -
                                            {{ sensor.observacoes[0].valor }} {{ sensor.unidade }}
                                        </li>
                                    </ul>
                                </div>
                                <div v-else>Nenhuma observação registada</div>
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
                <br />
                Sensores:
                <ul>
                    <li v-for="sensor in embalagens.sensores" :key="sensor.id">
                        {{ sensor.tipo }} - {{ sensor.unidade }}
                        <br />
                        <nuxt-link :to="`/sensores/${sensor.id}`">Ver mais detalhes</nuxt-link>
                        <br />
                        <div v-if="sensor.observacoes.length > 0">
                            Ultima observação:
                            <ul>
                                <li>
                                    {{ sensor.observacoes[0].timestamp }} -
                                    {{ sensor.observacoes[0].valor }} {{ sensor.unidade }}
                                </li>
                            </ul>
                        </div>
                        <div v-else>
                            <h4>Ainda nenhuma observação registada</h4>
                        </div>
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
import Navbar from "~/layouts/nav-bar.vue"
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
const route = useRoute()
const id = route.params.id
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: encomenda, error: proErr } = await useFetch(`${api}/encomendas/${id}`, {
    method: "GET",
    headers: { Authorization: "Bearer " + authStore.token },
})
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
