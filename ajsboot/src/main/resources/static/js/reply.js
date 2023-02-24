/* Axios를 사용하기위한 준비
 * async/await를 같이 이용하면 비동기처리를 동기화된 코드처럼 작성할수있다.
 * async는 함수선언시에 사용하는데 해당 함수가 비동기 처리를 위한 함수라는것을 명시하기위해사용하며
 * await는 async 하수내에서 비동기 호출하는 부분에 사용한다.
 */

async function get1(bno){
	const result = await axios.get(`/replies/list/${bno}`)
//	console.log(result)
	return result;
}

async function getList({bno,page,size,goLast}) {
	const result = await axios.get(`/replies/list/${bno}`,{params: {page, size}})
	return result.data
}
	