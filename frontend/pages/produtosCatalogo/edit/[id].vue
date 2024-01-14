<template>
  <div v-if="produto">
    <h2 v-once>Editar produto - {{ produto.nome }}</h2>

    <form @submit.prevent="updateProduto">
      <label for="nome">Nome:</label>
      <input v-model.trim="produtoForm.nome" type="text" />
      <span v-if="produtoForm.nome !== null && !isNameValid" class="error">
        ERRO: {{ formFeedback.nome }}</span>
      <br />
      <div>
        Fabricante:
        <select v-model.trim="produtoForm.fabricanteUsername">
          <option v-for="fabricante in fabricantes" :value="fabricante.username">
            {{ fabricante.nome }}
          </option>
        </select>
        <span v-if="produtoForm.fabricanteUsername !== null && !isFabricanteValid" class="error">
          ERRO: {{ formFeedback.fabricanteUsername }}</span>
      </div>
      <br />

      <button type="submit" :disabled="!isFormValid">Save</button>
    </form>
    <nuxt-link to="/produtosCatalogo">Back to produtos</nuxt-link>
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

const produtoForm = reactive({
  nome: null,
  fabricanteUsername: null,
});

const formFeedback = reactive({
  nome: "",
  fabricanteUsername: "",
});

const { data: fabricantes } = await useFetch(`${api}/fabricantes`);

const fetchProduto = async () => {
  try {
    const response = await fetch(`${api}/produtosCatalogo/${id}`);
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    produto.value = await response.json();
    produtoForm.nome = produto.value.nome;
    produtoForm.fabricanteUsername = produto.value.fabricanteUsername;
  } catch (error) {
    messages.value.push(error.message);
  }
};

const isNameValid = computed(() => {
  if (produtoForm.nome === null) {
    return false;
  }
  if (produtoForm.nome.length < 3) {
    formFeedback.nome = "O nome deve ter pelo menos 3 caracteres";
    return false;
  }
  if (produtoForm.nome.length > 20) {
    formFeedback.nome = "O nome deve ter no máximo 20 caracteres";
    return false;
  }
  formFeedback.nome = "";
  return true;
});

const isFabricanteValid = computed(() => {
  if (produtoForm.fabricanteUsername === null) {
    return false;
  }

  formFeedback.fabricanteUsername = "";
  return true;
});

const isFormValid = computed(() => {
  return isNameValid.value && isFabricanteValid.value;
});

const updateProduto = async () => {
  try {
    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(produtoForm),
    };

    const response = await fetch(`${api}/produtosCatalogo/${id}`, requestOptions);
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    navigateTo("/produtosCatalogo");
  } catch (error) {
    messages.value.push(error.message);
  }
};

fetchProduto();
</script>
