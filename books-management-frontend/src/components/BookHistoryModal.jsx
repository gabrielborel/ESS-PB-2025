import { useState, useEffect } from 'react'

function BookHistoryModal({ bookId, isOpen, onClose }) {
  const [history, setHistory] = useState([])
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState(null)

  useEffect(() => {
    if (isOpen && bookId) {
      fetchHistory()
    }
  }, [isOpen, bookId])

  const fetchHistory = async () => {
    setLoading(true)
    setError(null)
    try {
      const response = await fetch(`/api/books/${bookId}/history`)
      if (!response.ok) {
        throw new Error('Erro ao buscar histórico')
      }
      const data = await response.json()
      setHistory(data)
    } catch (err) {
      setError(err.message)
    } finally {
      setLoading(false)
    }
  }

  const formatDateTime = (dateString) => {
    if (!dateString) return 'N/A'
    const date = new Date(dateString)
    return date.toLocaleString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
      hour: '2-digit',
      minute: '2-digit',
    })
  }

  const getOperationBadgeColor = (operation) => {
    switch (operation) {
      case 'CREATE':
        return 'bg-green-100 text-green-800'
      case 'UPDATE':
        return 'bg-blue-100 text-blue-800'
      case 'DELETE':
        return 'bg-red-100 text-red-800'
      default:
        return 'bg-gray-100 text-gray-800'
    }
  }

  const getOperationLabel = (operation) => {
    switch (operation) {
      case 'CREATE':
        return 'Criação'
      case 'UPDATE':
        return 'Atualização'
      case 'DELETE':
        return 'Exclusão'
      default:
        return operation
    }
  }

  if (!isOpen) return null

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50 p-4">
      <div className="bg-white rounded-lg max-w-4xl w-full max-h-[90vh] overflow-hidden flex flex-col">
        <div className="p-6 border-b border-gray-200">
          <div className="flex justify-between items-center">
            <h2 className="text-2xl font-bold text-gray-900">Histórico de Alterações</h2>
            <button
              onClick={onClose}
              className="text-gray-400 hover:text-gray-600 transition-colors"
            >
              <svg className="w-6 h-6" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>
        </div>

        <div className="flex-1 overflow-y-auto p-6">
          {loading && (
            <div className="text-center py-8">
              <p className="text-gray-500">Carregando histórico...</p>
            </div>
          )}

          {error && (
            <div className="bg-red-50 border border-red-200 rounded-lg p-4">
              <p className="text-red-600">{error}</p>
            </div>
          )}

          {!loading && !error && history.length === 0 && (
            <div className="text-center py-8">
              <p className="text-gray-500">Nenhum histórico encontrado</p>
            </div>
          )}

          {!loading && !error && history.length > 0 && (
            <div className="space-y-4">
              {history.map((record, index) => (
                <div
                  key={record.id}
                  className="border border-gray-200 rounded-lg p-4 hover:shadow-md transition-shadow"
                >
                  <div className="flex items-start justify-between mb-3">
                    <div className="flex items-center gap-3">
                      <span
                        className={`px-3 py-1 rounded-full text-xs font-semibold ${getOperationBadgeColor(
                          record.operationType
                        )}`}
                      >
                        {getOperationLabel(record.operationType)}
                      </span>
                      <span className="text-sm text-gray-600">
                        {formatDateTime(record.timestamp)}
                      </span>
                    </div>
                  </div>

                  <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-3">
                    <div>
                      <p className="text-xs text-gray-500 font-medium mb-1">Título</p>
                      <p className="text-sm text-gray-900">{record.title}</p>
                    </div>
                    <div>
                      <p className="text-xs text-gray-500 font-medium mb-1">Autor</p>
                      <p className="text-sm text-gray-900">{record.author}</p>
                    </div>
                    <div>
                      <p className="text-xs text-gray-500 font-medium mb-1">Ano</p>
                      <p className="text-sm text-gray-900">{record.publicationYear}</p>
                    </div>
                  </div>

                  {record.changeDescription && (
                    <div className="mt-3 pt-3 border-t border-gray-100">
                      <p className="text-xs text-gray-500 font-medium mb-1">Descrição</p>
                      <p className="text-sm text-gray-700">{record.changeDescription}</p>
                    </div>
                  )}
                </div>
              ))}
            </div>
          )}
        </div>

        <div className="p-6 border-t border-gray-200">
          <button
            onClick={onClose}
            className="w-full bg-gray-600 text-white py-2 px-4 rounded-md hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 transition-colors font-medium"
          >
            Fechar
          </button>
        </div>
      </div>
    </div>
  )
}

export default BookHistoryModal
