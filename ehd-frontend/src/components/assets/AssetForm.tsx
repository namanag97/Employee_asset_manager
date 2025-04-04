'use client';

import { useState } from 'react';
import type { HardwareType } from '@/types';

interface AssetFormProps {
  hardwareTypes: HardwareType[];
  onSubmit: (data: FormData) => void;
  isLoading: boolean;
  error: string | null;
}

interface FormData {
  serialNumber: string;
  model: string;
  manufacturer: string;
  hardwareTypeId: string;
  purchaseDate: string;
  warrantyEndDate: string;
  notes?: string;
}

export default function AssetForm({ hardwareTypes, onSubmit, isLoading, error }: AssetFormProps) {
  const [formData, setFormData] = useState<FormData>({
    serialNumber: '',
    model: '',
    manufacturer: '',
    hardwareTypeId: '',
    purchaseDate: '',
    warrantyEndDate: '',
    notes: '',
  });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    onSubmit(formData);
  };

  const handleChange = (e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement | HTMLTextAreaElement>) => {
    const { name, value } = e.target;
    setFormData(prev => ({ ...prev, [name]: value }));
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-6">
      {error && (
        <div className="rounded-md bg-red-50 p-4">
          <div className="flex">
            <div className="ml-3">
              <h3 className="text-sm font-medium text-red-800">{error}</h3>
            </div>
          </div>
        </div>
      )}

      <div className="grid grid-cols-1 gap-x-6 gap-y-4 sm:grid-cols-2">
        <div>
          <label htmlFor="serialNumber" className="block text-sm font-medium leading-6 text-gray-900">
            Serial Number
          </label>
          <div className="mt-2">
            <input
              type="text"
              name="serialNumber"
              id="serialNumber"
              required
              value={formData.serialNumber}
              onChange={handleChange}
              className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
            />
          </div>
        </div>

        <div>
          <label htmlFor="hardwareTypeId" className="block text-sm font-medium leading-6 text-gray-900">
            Hardware Type
          </label>
          <div className="mt-2">
            <select
              id="hardwareTypeId"
              name="hardwareTypeId"
              required
              value={formData.hardwareTypeId}
              onChange={handleChange}
              className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
            >
              <option value="">Select a type</option>
              {hardwareTypes.map((type) => (
                <option key={type.id} value={type.id}>
                  {type.name}
                </option>
              ))}
            </select>
          </div>
        </div>

        <div>
          <label htmlFor="manufacturer" className="block text-sm font-medium leading-6 text-gray-900">
            Manufacturer
          </label>
          <div className="mt-2">
            <input
              type="text"
              name="manufacturer"
              id="manufacturer"
              required
              value={formData.manufacturer}
              onChange={handleChange}
              className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
            />
          </div>
        </div>

        <div>
          <label htmlFor="model" className="block text-sm font-medium leading-6 text-gray-900">
            Model
          </label>
          <div className="mt-2">
            <input
              type="text"
              name="model"
              id="model"
              required
              value={formData.model}
              onChange={handleChange}
              className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
            />
          </div>
        </div>

        <div>
          <label htmlFor="purchaseDate" className="block text-sm font-medium leading-6 text-gray-900">
            Purchase Date
          </label>
          <div className="mt-2">
            <input
              type="date"
              name="purchaseDate"
              id="purchaseDate"
              required
              value={formData.purchaseDate}
              onChange={handleChange}
              className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
            />
          </div>
        </div>

        <div>
          <label htmlFor="warrantyEndDate" className="block text-sm font-medium leading-6 text-gray-900">
            Warranty End Date
          </label>
          <div className="mt-2">
            <input
              type="date"
              name="warrantyEndDate"
              id="warrantyEndDate"
              required
              value={formData.warrantyEndDate}
              onChange={handleChange}
              className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
            />
          </div>
        </div>

        <div className="sm:col-span-2">
          <label htmlFor="notes" className="block text-sm font-medium leading-6 text-gray-900">
            Notes
          </label>
          <div className="mt-2">
            <textarea
              id="notes"
              name="notes"
              rows={3}
              value={formData.notes}
              onChange={handleChange}
              className="block w-full rounded-md border-0 py-1.5 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 placeholder:text-gray-400 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
            />
          </div>
        </div>
      </div>

      <div className="flex justify-end">
        <button
          type="submit"
          disabled={isLoading}
          className="rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 focus-visible:outline focus-visible:outline-2 focus-visible:outline-offset-2 focus-visible:outline-indigo-600 disabled:opacity-50 disabled:cursor-not-allowed"
        >
          {isLoading ? (
            <div className="h-5 w-5 animate-spin rounded-full border-2 border-white border-t-transparent" />
          ) : (
            'Create Asset'
          )}
        </button>
      </div>
    </form>
  );
} 