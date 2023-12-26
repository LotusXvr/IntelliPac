<template>
  <form @submit.prevent="create">
    <label for="username">Nome</label>
    <input id="username" v-model="produtoForm.nome" />
    <span class="error">{{ formFeedback.nome }}</span>
    <br />
    <div>
      Fabricante:
      <select v-model="produtoForm.idFabricante">
        <option value=''>--- Please select Fabricante ---</option>
        <option v-for="fabricante in fabricantes" :value="fabricante.id">
          {{ fabricante.nome }}
        </option>
      </select>
      <span v-if="produtoForm.idFabricante !== null && !isFabricanteValid" class="error">
        ERRO: {{ formFeedback.idFabricante }}</span>
    </div>

    <br />
    <button type="submit" :disabled="!isFormValid">Criar produto</button>
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
const produtoForm = reactive({
  nome: null,
  idFabricante: '', // Alterado de Number para aceitar nulos
});

const formFeedback = reactive({
  nome: "",
  idFabricante: "",
});

const config = useRuntimeConfig();
const api = config.public.API_URL;
const { data: fabricantes } = await useFetch(`${api}/fabricantes`);
const message = ref("");

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
  if (produtoForm.idFabricante === null) {
    return false;
  }

  formFeedback.idFabricante = "";
  return true;
});

const isFormValid = computed(() => {
  return isNameValid.value && isFabricanteValid.value;
});

async function create() {
  // Converte idFabricante para número
  produtoForm.idFabricante = Number(produtoForm.idFabricante);

  const requestOptions = {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(produtoForm),
  };

  const { error } = await useFetch(`${api}/produtos`, requestOptions);
  if (!error.value) navigateTo("/produtos");
  message.value = error.value;
}
</script>
