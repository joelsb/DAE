<template>
  <div v-if="error" class="error-message">
    Error: {{ error.message }}
  </div>
  <div>
    <h2>Unroll Subject</h2>
    <form @submit.prevent="unrollSubject">
      <label for="subjectCode">Select Subject:</label>
      <select v-model="selectedSubjectCode" required>
        <option v-for="subject in subjects" :value="subject.code">{{ subject.name }}</option>
      </select>
      <button type="submit">Unroll</button>
    </form>
    <div v-if="unenrollmentStatusMessage">{{ unenrollmentStatusMessage }}</div>
    <button @click.prevent="refresh" class="refresh-button">Refresh Data</button>
  </div>
</template>

<script setup>
const username = useRoute().params.username;
const config = useRuntimeConfig();
const apiEndpoint = config.public.API_URL;
const { data: subjects, error, refresh } = await useFetch(`${apiEndpoint}/subjects`);

const selectedSubjectCode = ref(null);
const unenrollmentStatusMessage = ref('');

const unrollSubject = async () => {
  try {
    const response = await axios.put(`${apiEndpoint}/students/${username}/unroll/${selectedSubjectCode.value}`);
    if (response.status === 200) {
      unenrollmentStatusMessage.value = 'Subject unenrolled successfully!';
    } else {
      unenrollmentStatusMessage.value = 'Failed to unroll from the subject. Please try again.';
    }
  } catch (error) {
    unenrollmentStatusMessage.value = 'Error occurred during subject unenrollment. Please try again later.';
  }
};
</script>
