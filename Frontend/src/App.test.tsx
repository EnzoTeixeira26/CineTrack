import React from 'react';
import { render } from '@testing-library/react';
import App from './App';

//teste de exemplo que verifica se o componente app renderiza sem quebrar. ( Eu pesquisei ent deletar ele e opcional )

test('renders without crashing', () => {
  const { baseElement } = render(<App />);
  expect(baseElement).toBeDefined();
});
