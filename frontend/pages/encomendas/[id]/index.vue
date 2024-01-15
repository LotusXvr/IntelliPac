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
                <ul>
                    <li v-for="embalagem in produto.embalagensDeProduto" :key="embalagem.id">
                        {{ embalagem.material }} ({{ embalagem.tipoEmbalagem }})
                    </li>
                </ul>
            </li>
        </ul>

        <p>Embalagens Transporte:</p>
        <ul v-for="embalagens in encomenda.embalagensTransporte" :key="embalagens.id">
            Material:
            {{
                embalagens.material
            }}
        </ul>
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
</script>
