<template>
  <div v-if="error">Error: {{ error.message }}</div>
  <div v-else>
    <nuxt-link to="produtosCatalogo/create">Criar novo Produto</nuxt-link>
    <h2>Produtos</h2>
    <table>
      <tr>
        <th>Nome</th>
        <th>Fabricante</th>
        <th>actions</th>
      </tr>
      <tr v-for="produto in produtos">
        <td>{{ produto.nome }}</td>
        <td>{{ produto.fabricanteUsername }}</td>
        <td>
          <nuxt-link :to="`/produtosCatalogo/${produto.id}`">Detalhes</nuxt-link>
          |
          <nuxt-link :to="'/produtosCatalogo/edit/' + produto.id">Editar</nuxt-link>
          |
          <button @click="deleteProduto(produto.id)">Excluir</button>
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
const config = useRuntimeConfig();
const api = config.public.API_URL;
const { data: produtos, error, refresh } = await useFetch(`${api}/produtosCatalogo`);

const deleteProduto = async (id) => {
  try {
    const response = await fetch(`${api}/produtosCatalogo/${id}`, {
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
