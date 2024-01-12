<template>
    <form @submit.prevent="create">
        <label for="valor">Valor: </label>
        <input id="valor" v-model="observacaoForm.valor" />
        <span class="error">{{ formFeedback.valor }}</span>
        <br />
        <div>
            Sensor:
            <select v-model="observacaoForm.sensorId">
                <option value=''>--- Please select Sensor ---</option>
                <option v-for="sensor in sensores" :value="sensor.idSensor">
                    {{ sensor.idSensor }} - {{ sensor.tipo }}
                </option>
            </select>
            <span v-if="observacaoForm.sensorId !== null && !isSensorValid" class="error">
                ERRO: {{ formFeedback.sensorId }}</span>
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
const observacaoForm = reactive({
    valor: null,
    sensorId: '', // Alterado de Number para aceitar nulos
});

const formFeedback = reactive({
    valor: "",
    sensorId: "",
});

const config = useRuntimeConfig();
const api = config.public.API_URL;
const { data: sensores } = await useFetch(`${api}/sensores`);
const message = ref("");

const isValorValid = computed(() => {
    if (observacaoForm.valor === null) {
        return false;
    }
    if (observacaoForm.valor.length < 1) {
        formFeedback.valor = "A observação deve ter pelo menos 1 caracteres";
        return false;
    }
    if (observacaoForm.valor.length > 5) {
        formFeedback.valor = "A observação deve ter no máximo 5 caracteres";
        return false;
    }
    formFeedback.valor = "";
    return true;
});

const isSensorValid = computed(() => {
    if (observacaoForm.sensorId === null) {
        return false;
    }
    if (observacaoForm.sensorId.length < 1) {
        formFeedback.sensorId = "Escolha um sensor";
        return false;
    }

    formFeedback.sensorId = "";
    return true;
});

const isFormValid = computed(() => {
    return isValorValid.value && isSensorValid.value;
});

async function create() {
    console.log(observacaoForm)
    observacaoForm.sensorId = observacaoForm.sensorId;

    const requestOptions = {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(observacaoForm),
    };

    const { error } = await useFetch(`${api}/observacoes`, requestOptions);
    if (!error.value) navigateTo("/observacoes");
    message.value = error.value;
}
</script>
  