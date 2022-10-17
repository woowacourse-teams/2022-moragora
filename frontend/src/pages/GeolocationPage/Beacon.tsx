const Beacon = ({ id, position, address, radius, panTo, remove }) => {
  return (
    <div className="beacon-list-item">
      <dl>
        <dt className="label">위치</dt>
        <dd>{address.address_name}</dd>
        <dt className="label">반경(m)</dt>
        <dd className="number">{radius}</dd>
      </dl>
      <button
        type="button"
        onClick={() => {
          panTo(position.Ma, position.La);
        }}
      >
        이동
      </button>
      <button type="button" onClick={() => remove(id)}>
        제거
      </button>
    </div>
  );
};

export default Beacon;
