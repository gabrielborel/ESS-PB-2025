import { useState, useEffect } from 'react'
import { getReviewsByBookId, getBookStats } from '../services/reviewService'
import ReviewForm from './ReviewForm'

export default function ReviewList({ bookId, bookTitle }) {
  const [reviews, setReviews] = useState([])
  const [stats, setStats] = useState(null)
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [showForm, setShowForm] = useState(false)

  useEffect(() => {
    loadReviews()
    loadStats()
  }, [bookId])

  const loadReviews = async () => {
    try {
      setLoading(true)
      const data = await getReviewsByBookId(bookId)
      setReviews(data)
      setError('')
    } catch (err) {
      setError('Erro ao carregar avaliações')
      console.error(err)
    } finally {
      setLoading(false)
    }
  }

  const loadStats = async () => {
    try {
      const data = await getBookStats(bookId)
      setStats(data)
    } catch (err) {
      console.error('Erro ao carregar estatísticas:', err)
    }
  }

  const handleReviewCreated = (newReview) => {
    setReviews((prev) => [newReview, ...prev])
    setShowForm(false)
    loadStats() // Recarregar estatísticas
  }

  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  }

  const renderStars = (rating) => {
    return [...Array(5)].map((_, index) => (
      <span
        key={index}
        className={`text-xl ${
          index < rating ? 'text-yellow-400' : 'text-gray-300'
        }`}
      >
        ★
      </span>
    ))
  }

  if (loading) {
    return (
      <div className="text-center py-8">
        <p className="text-gray-600">Carregando avaliações...</p>
      </div>
    )
  }

  return (
    <div className="space-y-6">
      <div className="bg-gradient-to-r from-blue-50 to-indigo-50 p-6 rounded-lg border border-blue-200">
        <h2 className="text-2xl font-bold text-gray-800 mb-2">
          Avaliações - {bookTitle}
        </h2>
        {stats && (
          <div className="flex items-center gap-6 mt-4">
            <div className="flex items-center gap-2">
              <div className="flex">{renderStars(Math.round(stats.averageRating))}</div>
              <span className="text-2xl font-bold text-gray-800">
                {stats.averageRating ? stats.averageRating.toFixed(1) : '0.0'}
              </span>
            </div>
            <div className="text-gray-600">
              <span className="font-semibold">{stats.totalReviews}</span>{' '}
              {stats.totalReviews === 1 ? 'avaliação' : 'avaliações'}
            </div>
          </div>
        )}
      </div>

      {!showForm && (
        <button
          onClick={() => setShowForm(true)}
          className="w-full bg-blue-600 text-white px-6 py-3 rounded-lg hover:bg-blue-700 transition-colors font-medium"
        >
          + Adicionar Avaliação
        </button>
      )}

      {showForm && (
        <ReviewForm
          bookId={bookId}
          onReviewCreated={handleReviewCreated}
          onCancel={() => setShowForm(false)}
        />
      )}

      {error && (
        <div className="p-4 bg-red-100 border border-red-400 text-red-700 rounded">
          {error}
        </div>
      )}

      {reviews.length === 0 ? (
        <div className="text-center py-12 bg-gray-50 rounded-lg border-2 border-dashed border-gray-300">
          <p className="text-gray-500 text-lg">
            Nenhuma avaliação ainda. Seja o primeiro a avaliar!
          </p>
        </div>
      ) : (
        <div className="space-y-4">
          {reviews.map((review) => (
            <div
              key={review.id}
              className="bg-white p-6 rounded-lg shadow-md border border-gray-200 hover:shadow-lg transition-shadow"
            >
              <div className="flex items-start justify-between mb-3">
                <div>
                  <h3 className="font-semibold text-lg text-gray-800">
                    {review.reviewerName}
                  </h3>
                  <p className="text-sm text-gray-500">
                    {formatDate(review.createdAt)}
                  </p>
                </div>
                <div className="flex">{renderStars(review.rating)}</div>
              </div>
              <p className="text-gray-700 leading-relaxed">{review.comment}</p>
            </div>
          ))}
        </div>
      )}
    </div>
  )
}
