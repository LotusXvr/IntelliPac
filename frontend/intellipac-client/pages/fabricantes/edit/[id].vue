<template>
  <div v-if="fabricante">
    <h2 v-once>Editar fabricante - {{ fabricante.nomeFabricante }}</h2>

    <form @submit.prevent="updateFabricante">
      <label for="nomeFabricante">Nome:</label>
      <input v-model.trim="fabricanteForm.nomeFabricante" type="text" />
      <span
        v-if="fabricanteForm.nomeFabricante !== null && !isNameValid"
        class="error"
      >
        ERRO: {{ formFeedback.nomeFabricante }}</span
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
  nomeFabricante: null,
});

const formFeedback = reactive({
  nomeFabricante: "",
});

const fetchFabricante = async () => {
  try {
    const response = await fetch(`${api}/fabricantes/${id}`);
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    fabricante.value = await response.json();
    fabricanteForm.nomeFabricante = fabricante.value.nomeFabricante;
  } catch (error) {
    messages.value.push(error.message);
  }
};

const isNameValid = computed(() => {
  if (fabricanteForm.nomeFabricante === null) {
    return false;
  }
  if (fabricanteForm.nomeFabricante.length < 3) {
    formFeedback.nomeFabricante = "O nome deve ter pelo menos 3 caracteres";
    return false;
  }
  if (fabricanteForm.nomeFabricante.length > 20) {
    formFeedback.nomeFabricante = "O nome deve ter no máximo 20 caracteres";
    return false;
  }
  formFeedback.nomeFabricante = "";
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
