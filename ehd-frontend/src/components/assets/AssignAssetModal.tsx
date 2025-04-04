'use client';

import { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { Combobox } from '@headlessui/react';
import { CheckIcon, ChevronUpDownIcon } from '@heroicons/react/20/solid';
import type { Employee } from '@/types';

interface AssignAssetModalProps {
  isOpen: boolean;
  onClose: () => void;
  assetId: string;
  onAssign: (employeeId: string) => void;
  isLoading: boolean;
}

async function fetchEmployees(): Promise<Employee[]> {
  const response = await fetch('/api/employees');
  if (!response.ok) {
    throw new Error('Failed to fetch employees');
  }
  return response.json();
}

export default function AssignAssetModal({
  isOpen,
  onClose,
  onAssign,
  isLoading,
}: AssignAssetModalProps) {
  const [query, setQuery] = useState('');
  const [selectedEmployee, setSelectedEmployee] = useState<Employee | null>(null);

  const { data: employees = [] } = useQuery({
    queryKey: ['employees'],
    queryFn: fetchEmployees,
  });

  const filteredEmployees = query === ''
    ? employees
    : employees.filter((employee) => {
        const searchString = `${employee.firstName} ${employee.lastName} ${employee.email}`.toLowerCase();
        return searchString.includes(query.toLowerCase());
      });

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (selectedEmployee) {
      onAssign(selectedEmployee.id);
    }
  };

  if (!isOpen) return null;

  return (
    <div className="fixed inset-0 z-50 overflow-y-auto">
      <div className="flex min-h-full items-end justify-center p-4 text-center sm:items-center sm:p-0">
        <div className="fixed inset-0 bg-gray-500 bg-opacity-75 transition-opacity" onClick={onClose} />

        <div className="relative transform overflow-hidden rounded-lg bg-white px-4 pb-4 pt-5 text-left shadow-xl transition-all sm:my-8 sm:w-full sm:max-w-lg sm:p-6">
          <div className="absolute right-0 top-0 hidden pr-4 pt-4 sm:block">
            <button
              type="button"
              className="rounded-md bg-white text-gray-400 hover:text-gray-500 focus:outline-none"
              onClick={onClose}
            >
              <span className="sr-only">Close</span>
              <svg className="h-6 w-6" fill="none" viewBox="0 0 24 24" strokeWidth="1.5" stroke="currentColor">
                <path strokeLinecap="round" strokeLinejoin="round" d="M6 18L18 6M6 6l12 12" />
              </svg>
            </button>
          </div>

          <div className="sm:flex sm:items-start">
            <div className="mt-3 text-center sm:mt-0 sm:text-left w-full">
              <h3 className="text-base font-semibold leading-6 text-gray-900">
                Assign Asset
              </h3>
              <div className="mt-2">
                <p className="text-sm text-gray-500">
                  Select an employee to assign this asset to.
                </p>
              </div>
            </div>
          </div>

          <form onSubmit={handleSubmit} className="mt-5">
            <div className="mt-2">
              <Combobox value={selectedEmployee} onChange={setSelectedEmployee}>
                <div className="relative">
                  <Combobox.Input
                    className="w-full rounded-md border-0 py-1.5 pl-3 pr-10 text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 focus:ring-2 focus:ring-inset focus:ring-indigo-600 sm:text-sm sm:leading-6"
                    onChange={(event: React.ChangeEvent<HTMLInputElement>) => setQuery(event.target.value)}
                    displayValue={(employee: Employee) => 
                      employee ? `${employee.firstName} ${employee.lastName} (${employee.email})` : ''
                    }
                    placeholder="Search employees..."
                  />
                  <Combobox.Button className="absolute inset-y-0 right-0 flex items-center rounded-r-md px-2 focus:outline-none">
                    <ChevronUpDownIcon className="h-5 w-5 text-gray-400" aria-hidden="true" />
                  </Combobox.Button>

                  {filteredEmployees.length > 0 && (
                    <Combobox.Options className="absolute z-10 mt-1 max-h-60 w-full overflow-auto rounded-md bg-white py-1 text-base shadow-lg ring-1 ring-black ring-opacity-5 focus:outline-none sm:text-sm">
                      {filteredEmployees.map((employee) => (
                        <Combobox.Option
                          key={employee.id}
                          value={employee}
                          className={({ active }: { active: boolean }) =>
                            `relative cursor-default select-none py-2 pl-3 pr-9 ${
                              active ? 'bg-indigo-600 text-white' : 'text-gray-900'
                            }`
                          }
                        >
                          {({ active, selected }: { active: boolean; selected: boolean }) => (
                            <>
                              <span className={`block truncate ${selected ? 'font-semibold' : ''}`}>
                                {employee.firstName} {employee.lastName} ({employee.email})
                              </span>
                              {selected && (
                                <span
                                  className={`absolute inset-y-0 right-0 flex items-center pr-4 ${
                                    active ? 'text-white' : 'text-indigo-600'
                                  }`}
                                >
                                  <CheckIcon className="h-5 w-5" aria-hidden="true" />
                                </span>
                              )}
                            </>
                          )}
                        </Combobox.Option>
                      ))}
                    </Combobox.Options>
                  )}
                </div>
              </Combobox>
            </div>

            <div className="mt-5 sm:mt-4 sm:flex sm:flex-row-reverse">
              <button
                type="submit"
                disabled={!selectedEmployee || isLoading}
                className="inline-flex w-full justify-center rounded-md bg-indigo-600 px-3 py-2 text-sm font-semibold text-white shadow-sm hover:bg-indigo-500 sm:ml-3 sm:w-auto disabled:opacity-50 disabled:cursor-not-allowed"
              >
                {isLoading ? 'Assigning...' : 'Assign Asset'}
              </button>
              <button
                type="button"
                className="mt-3 inline-flex w-full justify-center rounded-md bg-white px-3 py-2 text-sm font-semibold text-gray-900 shadow-sm ring-1 ring-inset ring-gray-300 hover:bg-gray-50 sm:mt-0 sm:w-auto"
                onClick={onClose}
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  );
} 