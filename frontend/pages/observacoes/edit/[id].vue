<template>
  <div v-if="observacao">
    <h2 v-once>Editar observacao - {{ observacao.id }}</h2>

    <form @submit.prevent="updateObservacao">
      <label for="valor">valor:</label>
      <input v-model.trim="observacaoForm.valor" type="text" />
      <span v-if="observacaoForm.valor !== null && !isValorValid" class="error">
        ERRO: {{ formFeedback.valor }}</span>
      <br />
      <div>
        sensor:
        <select v-model.trim="observacaoForm.sensorId">
          <option v-for="sensor in sensores" :value="sensor.idSensor">
            {{ sensor.idSensor }} - {{ sensor.tipo }}
          </option>
        </select>
        <span v-if="observacaoForm.sensorId !== null && !isSensorValid" class="error">
          ERRO: {{ formFeedback.sensorId }}</span>
      </div>
      <br />

      <button type="submit" :disabled="!isFormValid">Save</button>
    </form>
    <nuxt-link to="/observacoes">Back to Observações</nuxt-link>
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
import { useAuthStore } from "~/store/auth-store"
const authStore = useAuthStore()
const route = useRoute();
const id = route.params.id;
const config = useRuntimeConfig();
const api = config.public.API_URL;

const observacao = ref(null);
const messages = ref([]);

const observacaoForm = reactive({
  valor: null,
  sensorId: null,
});

const formFeedback = reactive({
  valor: "",
  sensorId: "",
});

const { data: sensores } = await useFetch(`${api}/sensores`, { method: "GET", headers: {'Authorization': 'Bearer ' + authStore.token}});

const fetchObservacoes = async () => {
  try {
    const response = await fetch(`${api}/observacoes/${id}`, { method: "GET", headers: {'Authorization': 'Bearer ' + authStore.token}});
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    observacao.value = await response.json();
    observacaoForm.valor = observacao.value.valor;
    observacaoForm.sensorId = observacao.value.sensorId;
  } catch (error) {
    messages.value.push(error.message);
  }
};

const isValorValid = computed(() => {
  if (observacaoForm.valor === null) {
    return false;
  }
  if (observacaoForm.valor.length < 1) {
    formFeedback.valor = "O valor deve ter pelo menos 1 caracter";
    return false;
  }
  if (observacaoForm.valor.length > 5) {
    formFeedback.valor = "O valor deve ter no máximo 5 caracteres";
    return false;
  }
  formFeedback.valor = "";
  return true;
});

const isSensorValid = computed(() => {
  if (observacaoForm.sensorId === null) {
    return false;
  }

  formFeedback.sensorId = "";
  return true;
});

const isFormValid = computed(() => {
  return isValorValid.value && isSensorValid.value;
});

const updateObservacao = async () => {
  try {
    const requestOptions = {
      method: "PUT",
      headers: { "Content-Type": "application/json", 'Authorization': 'Bearer ' + authStore.token },
      body: JSON.stringify(observacaoForm),
    };

    const response = await fetch(`${api}/observacoes/${id}`, requestOptions);
    if (!response.ok) {
      throw new Error(response.statusText);
    }
    navigateTo("/observacoes");
  } catch (error) {
    messages.value.push(error.message);
  }
};

fetchObservacoes();
</script>
