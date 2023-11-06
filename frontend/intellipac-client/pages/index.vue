<template>
  <div v-if="error">Error: {{ error.message }}</div>
  <div v-else>
    <nuxt-link to="/create">Create a New Product</nuxt-link>
    <h2>Produtos</h2>
    <table>
      <tr><th>Nome</th><th>Fabricante</th></tr>
      <tr v-for="produto in produtos">
        <td>{{ produto.nome }}</td>
        <td>{{ produto.nomeFabricante }}</td>
      </tr>
    </table>
  </div>
  <button @click.prevent="refresh">Refresh Data</button>
</template>
<script setup>
const config = useRuntimeConfig()
const api = config.public.API_URL
const {data: produtos, error, refresh} = await useFetch(`${api}/produtos`)
</script>