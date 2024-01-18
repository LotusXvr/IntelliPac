<template>
  <div v-if="fabricante">
    <h2>Detalhes de {{ fabricante.nome }}</h2>
    <p>Username: {{ fabricante.username }}</p>
    <p>Email: {{ fabricante.email }}</p>
    <p>Produtos:</p>
    <ul>
      <li v-for="produto in fabricante.produtos" :key="produto.id">
        {{ produto.nome }}
      </li>
    </ul>
  </div>

  <h2>Error messages:</h2>
  {{ messages }}
</template>
<script setup>
const route = useRoute();
const username = route.params.username;
const config = useRuntimeConfig();
const api = config.public.API_URL;
const { data: fabricante, error: fabricanteErr } = await useFetch(
  `${api}/fabricantes/${username}`
);
const messages = ref([]);
if (fabricanteErr.value) messages.value.push(fabricanteErr.value);
</script>
