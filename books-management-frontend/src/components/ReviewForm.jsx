import { useState } from 'react'

export default function ReviewForm({ bookId, onReviewCreated, onCancel }) {
  const [formData, setFormData] = useState({
    reviewerName: '',
    rating: 5,
    comment: '',
  })
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    setLoading(true)

    try {
      const response = await fetch('/api/reviews', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          bookId: bookId,
          ...formData,
        }),
      })

      if (!response.ok) {
        const errorData = await response.json()
        throw new Error(errorData.message || 'Erro ao criar avaliação')
      }

      const newReview = await response.json()
      onReviewCreated(newReview)
      
      // Resetar formulário
      setFormData({
        reviewerName: '',
        rating: 5,
        comment: '',
      })
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const handleChange = (e) => {
    const { name, value } = e.target
    setFormData((prev) => ({
      ...prev,
      [name]: name === 'rating' ? parseInt(value) : value,
    }))
  }

  return (
    <div className="bg-white p-6 rounded-lg shadow-md border border-gray-200">
      <h3 className="text-xl font-semibold mb-4 text-gray-800">
        Adicionar Avaliação
      </h3>

      {error && (
        <div className="mb-4 p-3 bg-red-100 border border-red-400 text-red-700 rounded">
          {error}
        </div>
      )}

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label
            htmlFor="reviewerName"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            Seu Nome
          </label>
          <input
            type="text"
            id="reviewerName"
            name="reviewerName"
            value={formData.reviewerName}
            onChange={handleChange}
            required
            maxLength={100}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            placeholder="Digite seu nome"
          />
        </div>

        <div>
          <label className="block text-sm font-medium text-gray-700 mb-2">
            Avaliação
          </label>
          <div className="flex items-center gap-2">
            {[1, 2, 3, 4, 5].map((star) => (
              <button
                key={star}
                type="button"
                onClick={() => setFormData((prev) => ({ ...prev, rating: star }))}
                onMouseEnter={(e) => {
                  const stars = e.currentTarget.parentElement.querySelectorAll('button')
                  stars.forEach((s, i) => {
                    s.querySelector('span').className = i < star 
                      ? 'text-3xl text-yellow-400 transition-all duration-150 scale-110' 
                      : 'text-3xl text-gray-300 transition-all duration-150'
                  })
                }}
                onMouseLeave={(e) => {
                  const stars = e.currentTarget.parentElement.querySelectorAll('button')
                  stars.forEach((s, i) => {
                    s.querySelector('span').className = i < formData.rating
                      ? 'text-3xl text-yellow-400 transition-all duration-150'
                      : 'text-3xl text-gray-300 transition-all duration-150'
                  })
                }}
                className="focus:outline-none hover:scale-110 transition-transform"
              >
                <span
                  className={`text-3xl transition-all duration-150 ${
                    star <= formData.rating ? 'text-yellow-400' : 'text-gray-300'
                  }`}
                >
                  ★
                </span>
              </button>
            ))}
            <span className="ml-2 text-lg font-semibold text-gray-700">
              {formData.rating} {formData.rating === 1 ? 'estrela' : 'estrelas'}
            </span>
          </div>
        </div>

        <div>
          <label
            htmlFor="comment"
            className="block text-sm font-medium text-gray-700 mb-1"
          >
            Comentário
          </label>
          <textarea
            id="comment"
            name="comment"
            value={formData.comment}
            onChange={handleChange}
            required
            maxLength={1000}
            rows={4}
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500 resize-none"
            placeholder="Escreva sua opinião sobre o livro..."
          />
          <p className="text-xs text-gray-500 mt-1">
            {formData.comment.length}/1000 caracteres
          </p>
        </div>

        <div className="flex gap-3">
          <button
            type="submit"
            disabled={loading}
            className="flex-1 bg-blue-600 text-white px-4 py-2 rounded-md hover:bg-blue-700 disabled:bg-gray-400 disabled:cursor-not-allowed transition-colors"
          >
            {loading ? 'Enviando...' : 'Enviar Avaliação'}
          </button>
          {onCancel && (
            <button
              type="button"
              onClick={onCancel}
              className="px-4 py-2 border border-gray-300 rounded-md hover:bg-gray-50 transition-colors"
            >
              Cancelar
            </button>
          )}
        </div>
      </form>
    </div>
  )
}
