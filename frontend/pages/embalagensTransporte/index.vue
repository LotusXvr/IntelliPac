<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="embalagensTransporte/create">Criar nova Embalagem De Transporte</nuxt-link>
        <h2>Embalagens De Transporte</h2>
        <table>
            <tr>
                <th>Id</th>
                <th>Material</th>
                <th>Altura(cm)</th>
                <th>Largura(cm)</th>
                <th>Comprimento(cm)</th>
                <th>Estado</th>
                <th>Sensores</th>
                <th>actions</th>
            </tr>
            <tr v-for="embalagemDeTransporte in embalagensDeTransporte">
                <td class="center">{{ embalagemDeTransporte.id }}</td>
                <td class="center">{{ embalagemDeTransporte.material }}</td>
                <td class="center">{{ embalagemDeTransporte.altura }}</td>
                <td class="center">{{ embalagemDeTransporte.largura }}</td>
                <td class="center">{{ embalagemDeTransporte.comprimento }}</td>
                <td class="center">{{ estadoToString(embalagemDeTransporte.estado) }}</td>
                <td class="center">{{ embalagemDeTransporte.sensores.length }}</td>
                <td class="right">
                    <nuxt-link :to="`/embalagensTransporte/${embalagemDeTransporte.id}`">Detalhes</nuxt-link>
                    |
                    <nuxt-link :to="'/embalagensTransporte/edit/' + embalagemDeTransporte.id">Editar</nuxt-link>
                    |
                    <nuxt-link :to="`/embalagensTransporte/${embalagemDeTransporte.id}` + '/Sensores'">
                        Sensores</nuxt-link>
                    <span v-if="hasEncomendas(embalagemDeTransporte) &&
                        embalagemDeTransporte.estado != 1
                        ">
                        |
                        <button @click="patchEstado(embalagemDeTransporte.id)">
                            Enviar encomendas
                        </button>
                    </span>
                    <span v-if="!hasEncomendas(embalagemDeTransporte)">
                        |
                        <button @click="deleteEncomenda(embalagemDeTransporte.id)">Excluir</button>
                    </span>
                </td>
            </tr>
        </table>
    </div>
    <br />
    <button @click.prevent="refresh">Refresh Data</button> <br />
    <br />
    <nuxt-link to="/">Voltar à Home</nuxt-link>
</template>
<style>
.center {
    text-align: center;
}

.right {
    text-align: right;
}
</style>
<script setup>
import Navbar from "~/layouts/nav-bar.vue"
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
const { user, token } = authStore
const config = useRuntimeConfig()
const api = config.public.API_URL
const {
    data: embalagensDeTransporte,
    error,
    refresh,
} = await useFetch(`${api}/embalagensDeTransporte`, {
    method: "GET",
    headers: { Authorization: "Bearer " + authStore.token },
})

const deleteEncomenda = async (id) => {
    try {
        const response = await fetch(`${api}/embalagensDeTransporte/${id}`, {
            method: "DELETE",
            headers: {
                Authorization: "Bearer " + authStore.token,
            },
        })
        if (response.ok) {
            refresh()
        }
    } catch (error) {
        console.error(error)
    }
}

const patchEstado = async (id) => {
    try {
        const response = await fetch(`${api}/embalagensDeTransporte/${id}/estado`, {
            method: "PATCH",
            headers: {
                "Content-Type": "application/json",
                Authorization: "Bearer " + authStore.token,
            },
            body: JSON.stringify({
                estado: 1,
            }),
        })
        if (response.ok) {
            refresh()
        }
    } catch (error) {
        console.error(error)
    }
}

const hasEncomendas = (embalagemDeTransporte) => {
    return embalagemDeTransporte.encomendas.length > 0
}

const estadoToString = (estado) => {
    switch (estado) {
        case 0:
            return "Disponivel"
        case 1:
            return "Em Transporte"
        default:
            return "Estado Inválido"
    }
}
</script>
