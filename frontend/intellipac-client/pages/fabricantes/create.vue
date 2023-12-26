<template>
  <form @submit.prevent="create">
    <label for="nome">Nome</label>
    <input id="nome" v-model="fabricanteForm.nome" />
    <span class="error">{{ formFeedback.nome }}</span>
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
  nome: null,
});

const formFeedback = reactive({
  nome: "",
});

const config = useRuntimeConfig();
const api = config.public.API_URL;
const message = ref("");

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
