<template>
  <div v-if="error">Error: {{ error.message }}</div>
  <div v-else>
    <nuxt-link to="/create">Create a New Product</nuxt-link>
    <h2>Produtos</h2>
    <table>
      <tr>
        <th>Nome</th>
        <th>Fabricante</th>
        <th>actions</th>
      </tr>
      <tr v-for="produto in produtos">
        <td>{{ produto.nome }}</td>
        <td>{{ produto.nomeFabricante }}</td>
        <td>
          <nuxt-link :to="`/produtos/${produto.id}`">Details</nuxt-link>
          |
          <nuxt-link :to="'/produtos/edit/' + produto.id">Editar</nuxt-link>
          |
          <button @click="deleteProduto(produto.id)">Excluir</button>
        </td>
      </tr>
    </table>
  </div>
  <br />
  <button @click.prevent="refresh">Refresh Data</button> <br />
  <br />
  <nuxt-link to="/">Back to Home</nuxt-link>
</template>
<script setup>
const config = useRuntimeConfig();
const api = config.public.API_URL;
const { data: produtos, error, refresh } = await useFetch(`${api}/produtos`);

const deleteProduto = async (id) => {
  try {
    const response = await fetch(`${api}/produtos/${id}`, {
      method: "DELETE",
    });
    if (response.ok) {
      refresh();
    }
  } catch (error) {
    console.error(error);
  }
};
</script>
