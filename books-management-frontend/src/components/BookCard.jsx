function BookCard({ book, onEdit, onDelete, onViewHistory, onViewReviews }) {
  const formatDate = (dateString) => {
    if (!dateString) return 'N/A'
    const date = new Date(dateString)
    return date.toLocaleDateString('pt-BR', {
      day: '2-digit',
      month: '2-digit',
      year: 'numeric',
    })
  }

  return (
    <div className="bg-white shadow-md rounded-lg p-6 hover:shadow-lg transition-shadow">
      <div className="mb-4">
        <h3 className="text-xl font-bold text-gray-900 mb-2 line-clamp-2">
          {book.title}
        </h3>
        <p className="text-gray-600 mb-1">
          <span className="font-medium">Autor:</span> {book.author}
        </p>
        <p className="text-gray-600 mb-1">
          <span className="font-medium">Ano:</span> {book.publicationYear}
        </p>
      </div>

      <div className="border-t pt-4 mb-4">
        <p className="text-xs text-gray-500 mb-1">
          <span className="font-medium">Criado em:</span> {formatDate(book.createdAt)}
        </p>
        <p className="text-xs text-gray-500">
          <span className="font-medium">Atualizado em:</span> {formatDate(book.updatedAt)}
        </p>
      </div>

      <div className="flex gap-2 mb-2">
        <button
          onClick={() => onEdit(book)}
          className="flex-1 bg-blue-600 text-white py-2 px-4 rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2 transition-colors text-sm font-medium"
        >
          Editar
        </button>
        <button
          onClick={() => onDelete(book.id)}
          className="flex-1 bg-red-600 text-white py-2 px-4 rounded-md hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-red-500 focus:ring-offset-2 transition-colors text-sm font-medium"
        >
          Excluir
        </button>
      </div>
      <div className="flex gap-2">
        <button
          onClick={() => onViewHistory(book.id)}
          className="flex-1 bg-gray-600 text-white py-2 px-4 rounded-md hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2 transition-colors text-sm font-medium"
        >
          Ver Histórico
        </button>
        <button
          onClick={() => onViewReviews(book)}
          className="flex-1 bg-purple-600 text-white py-2 px-4 rounded-md hover:bg-purple-700 focus:outline-none focus:ring-2 focus:ring-purple-500 focus:ring-offset-2 transition-colors text-sm font-medium"
        >
          Avaliações
        </button>
      </div>
    </div>
  )
}

export default BookCard
