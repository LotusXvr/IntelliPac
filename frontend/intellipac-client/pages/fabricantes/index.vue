<template>
  <div v-if="error">Error: {{ error.message }}</div>
  <div v-else>
    <nuxt-link to="fabricantes/create">Criar um novo fabricante</nuxt-link>
    <h2>Fabricantes</h2>
    <table>
      <tr>
        <th>Nome</th>
        <th>actions</th>
      </tr>
      <tr v-for="fabricante in fabricantes">
        <td>{{ fabricante.nome }}</td>
        <td>
          <nuxt-link :to="`/fabricantes/${fabricante.id}`">Details</nuxt-link>
          |
          <nuxt-link :to="'/fabricantes/edit/' + fabricante.id"
            >Editar</nuxt-link
          >
          |
          <button @click="deleteFabricante(fabricante.id)">Excluir</button>
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
const {
  data: fabricantes,
  error,
  refresh,
} = await useFetch(`${api}/fabricantes`);

const deleteFabricante = async (id) => {
  try {
    const response = await fetch(`${api}/fabricantes/${id}`, {
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
