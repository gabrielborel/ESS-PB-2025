import { useState, useEffect } from 'react'

function BookForm({ onSubmit, editingBook, onCancel }) {
  const [formData, setFormData] = useState({
    title: '',
    author: '',
    publicationYear: '',
  })
  const [errors, setErrors] = useState({})
  const [submitting, setSubmitting] = useState(false)

  useEffect(() => {
    if (editingBook) {
      setFormData({
        title: editingBook.title,
        author: editingBook.author,
        publicationYear: editingBook.publicationYear,
      })
    } else {
      setFormData({
        title: '',
        author: '',
        publicationYear: '',
      })
    }
  }, [editingBook])

  const validate = () => {
    const newErrors = {}

    if (!formData.title.trim()) {
      newErrors.title = 'O título é obrigatório'
    }

    if (!formData.author.trim()) {
      newErrors.author = 'O autor é obrigatório'
    }

    if (!formData.publicationYear) {
      newErrors.publicationYear = 'O ano é obrigatório'
    } else {
      const year = parseInt(formData.publicationYear)
      if (year < 1000 || year > 2100) {
        newErrors.publicationYear = 'O ano deve estar entre 1000 e 2100'
      }
    }

    setErrors(newErrors)
    return Object.keys(newErrors).length === 0
  }

  const handleSubmit = async (e) => {
    e.preventDefault()
    
    if (!validate()) {
      return
    }

    setSubmitting(true)
    try {
      const bookData = {
        title: formData.title.trim(),
        author: formData.author.trim(),
        publicationYear: parseInt(formData.publicationYear),
      }

      if (editingBook) {
        await onSubmit(editingBook.id, bookData)
      } else {
        await onSubmit(bookData)
      }

      setFormData({
        title: '',
        author: '',
        publicationYear: '',
      })
      setErrors({})
    } catch (err) {
      console.error('Erro ao salvar:', err)
    } finally {
      setSubmitting(false)
    }
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData(prev => ({
      ...prev,
      [name]: value
    }))
    if (errors[name]) {
      setErrors(prev => ({
        ...prev,
        [name]: ''
      }))
    }
  }

  const handleCancel = () => {
    setFormData({
      title: '',
      author: '',
      publicationYear: '',
    })
    setErrors({})
    onCancel()
  }

  return (
    <div className="bg-white shadow-md rounded-lg p-6">
      <h2 className="text-2xl font-bold mb-6 text-gray-800">
        {editingBook ? 'Editar' : 'Novo Livro'}
      </h2>
      
      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label htmlFor="title" className="block text-sm font-medium text-gray-700 mb-1">
            Título
          </label>
          <input
            type="text"
            id="title"
            name="title"
            value={formData.title}
            onChange={handleChange}
            className={`w-full px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 ${
              errors.title ? 'border-red-500' : 'border-gray-300'
            }`}
            placeholder="Título do livro"
          />
          {errors.title && (
            <p className="mt-1 text-sm text-red-600">{errors.title}</p>
          )}
        </div>

        <div>
          <label htmlFor="author" className="block text-sm font-medium text-gray-700 mb-1">
            Autor
          </label>
          <input
            type="text"
            id="author"
            name="author"
            value={formData.author}
            onChange={handleChange}
            className={`w-full px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 ${
              errors.author ? 'border-red-500' : 'border-gray-300'
            }`}
            placeholder="Nome do autor"
          />
          {errors.author && (
            <p className="mt-1 text-sm text-red-600">{errors.author}</p>
          )}
        </div>

        <div>
          <label htmlFor="publicationYear" className="block text-sm font-medium text-gray-700 mb-1">
            Ano
          </label>
          <input
            type="number"
            id="publicationYear"
            name="publicationYear"
            value={formData.publicationYear}
            onChange={handleChange}
            className={`w-full px-3 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500 ${
              errors.publicationYear ? 'border-red-500' : 'border-gray-300'
            }`}
            placeholder="Ano de publicação"
            min="1000"
            max="2100"
          />
          {errors.publicationYear && (
            <p className="mt-1 text-sm text-red-600">{errors.publicationYear}</p>
          )}
        </div>

        <div className="flex gap-3 pt-2">
          <button
            type="submit"
            disabled={submitting}
            className="flex-1 bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 disabled:opacity-50 disabled:cursor-not-allowed transition-colors"
          >
            {submitting ? 'Salvando...' : editingBook ? 'Salvar' : 'Adicionar'}
          </button>
          
          {editingBook && (
            <button
              type="button"
              onClick={handleCancel}
              className="flex-1 bg-gray-300 text-gray-700 py-2 px-4 rounded-md hover:bg-gray-400 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 transition-colors"
            >
              Cancelar
            </button>
          )}
        </div>
      </form>
    </div>
  )
}

export default BookForm
