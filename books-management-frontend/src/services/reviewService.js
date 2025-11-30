const API_BASE_URL = '/api/reviews'

export const getAllReviews = async () => {
  const response = await fetch(API_BASE_URL)
  if (!response.ok) {
    throw new Error('Erro ao buscar avaliações')
  }
  return response.json()
}

export const getReviewById = async (id) => {
  const response = await fetch(`${API_BASE_URL}/${id}`)
  if (!response.ok) {
    throw new Error('Erro ao buscar avaliação')
  }
  return response.json()
}

export const getReviewsByBookId = async (bookId) => {
  const response = await fetch(`${API_BASE_URL}/book/${bookId}`)
  if (!response.ok) {
    throw new Error('Erro ao buscar avaliações do livro')
  }
  return response.json()
}

export const getBookStats = async (bookId) => {
  const response = await fetch(`${API_BASE_URL}/book/${bookId}/stats`)
  if (!response.ok) {
    throw new Error('Erro ao buscar estatísticas do livro')
  }
  return response.json()
}

export const createReview = async (reviewData) => {
  const response = await fetch(API_BASE_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(reviewData),
  })
  if (!response.ok) {
    const error = await response.json()
    throw new Error(error.message || 'Erro ao criar avaliação')
  }
  return response.json()
}

export const updateReview = async (id, reviewData) => {
  const response = await fetch(`${API_BASE_URL}/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(reviewData),
  })
  if (!response.ok) {
    const error = await response.json()
    throw new Error(error.message || 'Erro ao atualizar avaliação')
  }
  return response.json()
}

export const deleteReview = async (id) => {
  const response = await fetch(`${API_BASE_URL}/${id}`, {
    method: 'DELETE',
  })
  if (!response.ok) {
    throw new Error('Erro ao deletar avaliação')
  }
}
