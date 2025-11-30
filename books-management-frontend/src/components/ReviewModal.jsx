import { useState } from 'react'
import ReviewList from './ReviewList'

export default function ReviewModal({ book, onClose }) {
  if (!book) return null

  return (
    <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center p-4 z-50">
      <div className="bg-white rounded-lg max-w-4xl w-full max-h-[90vh] overflow-hidden flex flex-col">
        <div className="bg-gradient-to-r from-blue-600 to-indigo-600 text-white p-6">
          <div className="flex justify-between items-start">
            <div>
              <h2 className="text-2xl font-bold mb-2">{book.title}</h2>
              <p className="text-blue-100">por {book.author}</p>
            </div>
            <button
              onClick={onClose}
              className="text-white hover:text-gray-200 text-3xl leading-none"
            >
              ×
            </button>
          </div>
        </div>

        <div className="flex-1 overflow-y-auto p-6">
          <ReviewList bookId={book.id} bookTitle={book.title} />
        </div>
      </div>
    </div>
  )
}
