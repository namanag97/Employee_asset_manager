import { render, screen, fireEvent } from '@testing-library/react';
import AssetForm from '../AssetForm';
import type { HardwareType } from '@/types';

describe('AssetForm', () => {
  const mockHardwareTypes: HardwareType[] = [
    { id: '1', name: 'Laptop' },
    { id: '2', name: 'Desktop' },
    { id: '3', name: 'Monitor' },
  ];

  const mockOnSubmit = jest.fn();

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('renders all form fields correctly', () => {
    render(
      <AssetForm
        hardwareTypes={mockHardwareTypes}
        onSubmit={mockOnSubmit}
        isLoading={false}
        error={null}
      />
    );

    // Check if all required fields are present
    expect(screen.getByLabelText(/serial number/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/hardware type/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/manufacturer/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/model/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/purchase date/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/warranty end date/i)).toBeInTheDocument();
    expect(screen.getByLabelText(/notes/i)).toBeInTheDocument();
    expect(screen.getByRole('button', { name: /create asset/i })).toBeInTheDocument();
  });

  it('displays error message when error prop is provided', () => {
    const errorMessage = 'Failed to create asset';
    render(
      <AssetForm
        hardwareTypes={mockHardwareTypes}
        onSubmit={mockOnSubmit}
        isLoading={false}
        error={errorMessage}
      />
    );

    expect(screen.getByText(errorMessage)).toBeInTheDocument();
  });

  it('disables submit button when loading', () => {
    render(
      <AssetForm
        hardwareTypes={mockHardwareTypes}
        onSubmit={mockOnSubmit}
        isLoading={true}
        error={null}
      />
    );

    const submitButton = screen.getByRole('button', { name: /create asset/i });
    expect(submitButton).toBeDisabled();
  });

  it('handles input changes correctly', () => {
    render(
      <AssetForm
        hardwareTypes={mockHardwareTypes}
        onSubmit={mockOnSubmit}
        isLoading={false}
        error={null}
      />
    );

    // Test text input
    const serialNumberInput = screen.getByLabelText(/serial number/i);
    fireEvent.change(serialNumberInput, { target: { name: 'serialNumber', value: 'SN123' } });
    expect(serialNumberInput).toHaveValue('SN123');

    // Test select input
    const hardwareTypeSelect = screen.getByLabelText(/hardware type/i);
    fireEvent.change(hardwareTypeSelect, { target: { name: 'hardwareTypeId', value: '1' } });
    expect(hardwareTypeSelect).toHaveValue('1');

    // Test date input
    const purchaseDateInput = screen.getByLabelText(/purchase date/i);
    fireEvent.change(purchaseDateInput, { target: { name: 'purchaseDate', value: '2024-01-01' } });
    expect(purchaseDateInput).toHaveValue('2024-01-01');

    // Test textarea
    const notesTextarea = screen.getByLabelText(/notes/i);
    fireEvent.change(notesTextarea, { target: { name: 'notes', value: 'Test notes' } });
    expect(notesTextarea).toHaveValue('Test notes');
  });

  it('submits form data correctly', () => {
    render(
      <AssetForm
        hardwareTypes={mockHardwareTypes}
        onSubmit={mockOnSubmit}
        isLoading={false}
        error={null}
      />
    );

    // Fill in form fields
    fireEvent.change(screen.getByLabelText(/serial number/i), {
      target: { name: 'serialNumber', value: 'SN123' },
    });
    fireEvent.change(screen.getByLabelText(/hardware type/i), {
      target: { name: 'hardwareTypeId', value: '1' },
    });
    fireEvent.change(screen.getByLabelText(/manufacturer/i), {
      target: { name: 'manufacturer', value: 'Dell' },
    });
    fireEvent.change(screen.getByLabelText(/model/i), {
      target: { name: 'model', value: 'XPS 13' },
    });
    fireEvent.change(screen.getByLabelText(/purchase date/i), {
      target: { name: 'purchaseDate', value: '2024-01-01' },
    });
    fireEvent.change(screen.getByLabelText(/warranty end date/i), {
      target: { name: 'warrantyEndDate', value: '2025-01-01' },
    });
    fireEvent.change(screen.getByLabelText(/notes/i), {
      target: { name: 'notes', value: 'Test notes' },
    });

    // Submit form
    fireEvent.click(screen.getByRole('button', { name: /create asset/i }));

    // Verify form data was submitted correctly
    expect(mockOnSubmit).toHaveBeenCalledWith({
      serialNumber: 'SN123',
      hardwareTypeId: '1',
      manufacturer: 'Dell',
      model: 'XPS 13',
      purchaseDate: '2024-01-01',
      warrantyEndDate: '2025-01-01',
      notes: 'Test notes',
    });
  });
}); 