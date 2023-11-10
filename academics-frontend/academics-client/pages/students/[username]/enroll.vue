<template>
  <div v-if="error" class="error-message">
    Error: {{ error.message }}
  </div>
  <div>
    <h2>Enroll Subject</h2>
    <form @submit.prevent="enrollSubject">
      <label for="subjectCode">Select Subject:</label>
      <select v-model="selectedSubjectCode" required>
        <option v-for="subject in subjects" :value="subject.code">{{ subject.name }}</option>
      </select>
      <button type="submit">Enroll</button>
    </form>
    <div v-if="enrollmentStatusMessage">{{ enrollmentStatusMessage }}</div>
    <button @click.prevent="refresh" class="refresh-button">Refresh Data</button>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue';
import axios from 'axios';
import { useRoute } from 'vue-router';

const username = useRoute().params.username;
const config = useRuntimeConfig();
const apiEndpoint = config.public.API_URL;
const { data: subjects, error, refresh } = await useFetch(`${apiEndpoint}/subjects`);

const selectedSubjectCode = ref(null);
const enrollmentStatusMessage = ref('');

const enrollSubject = async () => {
  try {
    const response = await axios.put(`${apiEndpoint}/students/${username}/enroll/${selectedSubjectCode.value}`);
    if (response.status === 200) {
      enrollmentStatusMessage.value = 'Subject enrolled successfully!';
    } else {
      enrollmentStatusMessage.value = 'Failed to enroll in the subject. Please try again.';
    }
  } catch (error) {
    enrollmentStatusMessage.value = 'Error occurred during subject enrollment. Please try again later.';
  }
};
</script>
