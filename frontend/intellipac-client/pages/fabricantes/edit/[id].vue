<template>
  <div v-if="fabricante">
    <h2 v-once>Editar fabricante - {{ fabricante.nome }}</h2>

    <form @submit.prevent="updateFabricante">
      <label for="nome">Nome:</label>
      <input v-model.trim="fabricanteForm.nome" type="text" />
      <span v-if="fabricanteForm.nome !== null && !isNameValid" class="error">
        ERRO: {{ formFeedback.nome }}</span
      >
      <br />
      <button type="submit" :disabled="!isFormValid">Save</button>
    </form>
    <nuxt-link to="/fabricantes">Voltar aos fabricantes</nuxt-link>
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

const fabricante = ref(null);
const messages = ref([]);

const fabricanteForm = reactive({
  nome: null,
});

const formFeedback = reactive({
  nome: "",
});

const fetchFabricante = async () => {
  try {
    const response = await fetch(`${api}/fabricantes/${id}`);
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    fabricante.value = await response.json();
    fabricanteForm.nome = fabricante.value.nome;
  } catch (error) {
    messages.value.push(error.message);
  }
};

const isNameValid = computed(() => {
  if (fabricanteForm.nome === null) {
    return false;
  }
  if (fabricanteForm.nome.length < 3) {
    formFeedback.nome = "O nome deve ter pelo menos 3 caracteres";
    return false;
  }
  if (fabricanteForm.nome.length > 20) {
    formFeedback.nome = "O nome deve ter no máximo 20 caracteres";
    return false;
  }
  formFeedback.nome = "";
  return true;
});

const isFormValid = computed(() => {
  return isNameValid.value;
});

const updateFabricante = async () => {
  try {
    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(fabricanteForm),
    };

    const response = await fetch(`${api}/fabricantes/${id}`, requestOptions);
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    navigateTo("/fabricantes");
  } catch (error) {
    messages.value.push(error.message);
  }
};

fetchFabricante();
</script>
