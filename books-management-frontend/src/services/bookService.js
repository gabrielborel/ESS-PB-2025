const API_BASE_URL = '/api/books'

export const getAllBooks = async () => {
  const response = await fetch(API_BASE_URL)
  if (!response.ok) {
    throw new Error('Erro ao buscar livros')
  }
  return response.json()
}

export const getBookById = async (id) => {
  const response = await fetch(`${API_BASE_URL}/${id}`)
  if (!response.ok) {
    throw new Error('Erro ao buscar livro')
  }
  return response.json()
}

export const createBook = async (bookData) => {
  const response = await fetch(API_BASE_URL, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(bookData),
  })
  if (!response.ok) {
    const error = await response.json()
    throw new Error(error.message || 'Erro ao criar livro')
  }
  return response.json()
}

export const updateBook = async (id, bookData) => {
  const response = await fetch(`${API_BASE_URL}/${id}`, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify(bookData),
  })
  if (!response.ok) {
    const error = await response.json()
    throw new Error(error.message || 'Erro ao atualizar livro')
  }
  return response.json()
}

export const deleteBook = async (id) => {
  const response = await fetch(`${API_BASE_URL}/${id}`, {
    method: 'DELETE',
  })
  if (!response.ok) {
    throw new Error('Erro ao excluir livro')
  }
}

export const getBookHistory = async (id) => {
  const response = await fetch(`${API_BASE_URL}/${id}/history`)
  if (!response.ok) {
    throw new Error('Erro ao buscar histórico do livro')
  }
  return response.json()
}
