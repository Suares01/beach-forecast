'use client';

import { useAuth } from '@src/contexts/AuthContext';

export default function Forecasts() {
  const { user, logout } = useAuth();

  return (
    <>
      <ul>
        {user &&
          Object.entries(user).map(([key, value]) => (
            <ul key={key}>
              {key}: {value}
            </ul>
          ))}
      </ul>
      <button onClick={() => logout()}>Sair</button>
    </>
  );
}
