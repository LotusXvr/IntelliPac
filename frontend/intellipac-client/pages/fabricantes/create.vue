<template>
  <form @submit.prevent="create">
    <label for="nomeFabricante">Nome</label>
    <input id="nomeFabricante" v-model="fabricanteForm.nomeFabricante" />
    <span class="error">{{ formFeedback.nomeFabricante }}</span>
    <br />
    <button type="submit" :disabled="!isFormValid">Criar fabricante</button>
  </form>
  {{ message }}
</template>
<style scoped>
.error {
  color: red;
}
</style>
<script setup>
import { ref, reactive, computed } from "vue";
const fabricanteForm = reactive({
  nomeFabricante: null,
});

const formFeedback = reactive({
  nomeFabricante: "",
});

const config = useRuntimeConfig();
const api = config.public.API_URL;
const message = ref("");

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

async function create() {
  const requestOptions = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(fabricanteForm),
  };

  const { error } = await useFetch(`${api}/fabricantes`, requestOptions);
  if (!error.value) navigateTo("/fabricantes");
  message.value = error.value;
}
</script>
