<template>
  <div v-if="produto">
    <h2 v-once>Edit produto - {{ produto.nome }}</h2>
  </div>
  <h2>Error messages:</h2>
  {{ messages }}
</template>
<style scoped>
.error {
  color: red;
}
</style>
<script setup>
const route = useRoute();
const id = route.params.id;
const config = useRuntimeConfig();
const api = config.public.API_URL;

const produto = ref(null);
const messages = ref([]);

const fetchProduto = async () => {
  try {
    const response = await fetch(`${api}/produtos/${id}`);
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    produto.value = await response.json();
  } catch (error) {
    messages.value.push(error.message);
  }
};

fetchProduto();
</script>
