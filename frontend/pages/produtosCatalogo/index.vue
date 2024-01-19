<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="produtosCatalogo/create" v-if="user !== null && user.role != 'Cliente'">Criar novo
            Produto</nuxt-link>
        <h2>Produtos</h2>
        <table>
            <tr>
                <th>Nome</th>
                <th>Fabricante</th>
                <th>Peso</th>
                <th v-if="user.role == 'FabricanteDeProdutos'">actions</th>
            </tr>
            <tr v-for=" produto  in  produtos ">
                <td>{{ produto.nome }}</td>
                <td>{{ produto.fabricanteUsername }}</td>
                <td>{{ produto.peso }}</td>
                <td style="display: flex; align-items: center;" v-if="user.role == 'FabricanteDeProdutos'">
                    <nuxt-link :to="`/produtosCatalogo/${produto.id}`">Detalhes</nuxt-link>
                    <div
                        v-if="user !== null && user.role == 'FabricanteDeProdutos' && user.username == produto.fabricanteUsername">
                        |
                        <nuxt-link :to="'/produtosCatalogo/edit/' + produto.id">Editar</nuxt-link>
                        |
                        <button @click="deleteProduto(produto.id)">Excluir</button>
                    </div>
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
import { useAuthStore } from "../store/auth-store.js"
import Navbar from "~/layouts/nav-bar.vue";
const config = useRuntimeConfig()
const authStore = useAuthStore()
const { user } = authStore
const api = config.public.API_URL
const { data: produtos, error, refresh } = await useFetch(`${api}/produtosCatalogo/fabricante/${user.username}`, { method: "GET", headers: {'Authorization': 'Bearer ' + authStore.token}})

const deleteProduto = async (id) => {
    try {
        const response = await fetch(`${api}/produtosCatalogo/${id}`, {
            method: "DELETE",
            headers: {
                'Authorization': 'Bearer ' + authStore.token
            }
        })
        if (response.ok) {
            refresh()
        }
    } catch (error) {
        console.error(error)
    }
}
</script>
