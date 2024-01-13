<template>
    <Navbar />
    <div v-if="error">Error: {{ error.message }}</div>
    <div v-else>
        <nuxt-link to="embalagens/create">Criar nova embalagem</nuxt-link>
        <h2>Embalagens</h2>
        <table>
            <tr>
                <th>id</th>
                <th>Material</th>
            </tr>
            <tr v-for="embalagem in embalagens">
                <td>{{ embalagem.id }}</td>
                <td>{{ embalagem.material }}</td>

                <td>
                    <nuxt-link :to="`/embalagens/${embalagem.id}`">Detalhes</nuxt-link>
                    |
                    <nuxt-link :to="'/embalagens/edit/' + embalagem.id">Editar</nuxt-link>
                    |
                    <button @click="deleteembalagem(embalagem.id)">Excluir</button>
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
import Navbar from "~/layouts/nav-bar.vue";
const config = useRuntimeConfig()
const api = config.public.API_URL
const { data: embalagens, error, refresh } = await useFetch(`${api}/embalagens`)

const deleteembalagem = async (id) => {
    try {
        const response = await fetch(`${api}/embalagens/${id}`, {
            method: "DELETE",
        })
        if (response.ok) {
            refresh()
        }
    } catch (error) {
        console.error(error)
    }
}
</script>
