<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="embalagensProduto/create">Criar um novo tipo embalagem</nuxt-link>
        <h2>Tipo Embalagens de Produto</h2>
        <table>
            <tr>
                <th>Tipo De Embalagem</th>
                <th>Material</th>
                <th>Dimensão (cm)</th>
                <th>Actions</th>
            </tr>
            <tr v-for="embalagem in embalagens">
                <td>{{ tipoToString( embalagem.tipo)}}</td>
                <td>{{ embalagem.material }}</td>
                <td>{{ embalagem.comprimento }}x{{ embalagem.largura }}x{{ embalagem.altura }}</td>

                <td>
                    <nuxt-link :to="`/embalagensProduto/${embalagem.id}`">Detalhes</nuxt-link>
                    |
                    <nuxt-link :to="'/embalagensProduto/edit/' + embalagem.id">Editar</nuxt-link>
                    |
                    <nuxt-link :to="`/embalagensProduto/${embalagem.id}/sensores`">Sensores</nuxt-link>
                    |
                    <button @click="deleteEmbalagem(embalagem.id)">Excluir</button>
                </td>
            </tr>
        </table>
    </div>
    <br />
    <button @click.prevent="refresh">Refresh Data</button> <br />
    <br />
    <nuxt-link to="/">Voltar à Home</nuxt-link>
</template>
<script setup>
import Navbar from "~/layouts/nav-bar.vue"
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: embalagens, error, refresh } = await useFetch(`${api}/tipoEmbalagens`, { method: "GET", headers: {'Authorization': 'Bearer ' + authStore.token}})


const deleteEmbalagem = async (id) => {
    try {
        const response = await fetch(`${api}/tipoEmbalagens/${id}`, {
            method: "DELETE",
            headers: {'Authorization': 'Bearer ' + authStore.token}
        })
        if (response.ok) {
            refresh()
        }
    } catch (error) {
        console.error(error)
    }
}

const tipoToString = (tipo) => {
    switch (tipo) {
        case 1:
            return "Primaria"
        case 2:
            return "Secundaria"
        case 3:
            return "Terciaria"
        default:
            return "Tipo não definido"
    }


}
</script>
